package br.com.cofredigital.crypto;

import java.io.ByteArrayOutputStream;

public class Base32 {
    private static final String ALFABETO_BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";

    /**
     * Converte dados binários para uma String codificada em Base32
     *
     * @param bytes Um array contendo dados binários
     * @return Uma String contendo os dados codificados
     */
    public static String encodeToString(byte[] bytes) {
        StringBuilder codificado = new StringBuilder();
        int i = 0, indice = 0, digito = 0;
        int byteAtual;

        while (i < bytes.length) {
            byteAtual = bytes[i] >= 0 ? bytes[i] : bytes[i] + 256;

            if (indice > 3) {
                if ((i + 1) < bytes.length) byteAtual = byteAtual << 8 | (bytes[i + 1] >= 0 ? bytes[i + 1] : bytes[i + 1] + 256);
                if ((i + 2) < bytes.length) byteAtual = byteAtual << 8 | (bytes[i + 2] >= 0 ? bytes[i + 2] : bytes[i + 2] + 256);
                i += 3;
                indice = 0;
            } else {
                i++;
            }

            digito = (indice == 0) ? (byteAtual & 0x1F) : ((indice == 1) ? ((byteAtual >> 5) & 0x1F) : ((indice == 2) ? ((byteAtual >> 10) & 0x1F) : ((byteAtual >> 15) & 0x1F)));
            indice = (indice + 1) % 4;
            codificado.append(ALFABETO_BASE32.charAt(digito));
        }

        return codificado.toString();
    }

    /**
     * Converte uma String codificada em Base32 para dados binários
     *
     * @param stringBase32 Uma String contendo os dados codificados
     * @return Um array contendo os dados binários, ou null se a string for inválida
     */
    public static byte[] decodificar(String stringBase32) {
        ByteArrayOutputStream decodificado = new ByteArrayOutputStream();

        int i = 0, indice = 0, consulta;
        int offset, digito;

        while (i < stringBase32.length()) {
            consulta = stringBase32.charAt(i++) - '0';
            if (consulta < 0 || consulta >= ALFABETO_BASE32.length()) {
                return null;
            }

            digito = ALFABETO_BASE32.charAt(consulta);
            if (digito == -1) {
                return null;
            }

            if (indice <= 3) {
                indice = (indice + 1) % 4;
                if (indice == 0) {
                    offset = 0;
                    for (int j = 0; j < 5; ++j) {
                        if (i + j >= stringBase32.length()) {
                            return null;
                        }
                        consulta = stringBase32.charAt(i + j) - '0';
                        if (consulta < 0 || consulta >= ALFABETO_BASE32.length()) {
                            return null;
                        }
                        digito = ALFABETO_BASE32.charAt(consulta);
                        if (digito == -1) {
                            return null;
                        }
                        offset = (offset << 5) | digito;
                    }
                    decodificado.write((offset >> 4) & 0xFF);
                    decodificado.write((offset & 0x0F) << 4);
                }
            }
        }

        return decodificado.toByteArray();
    }
}
