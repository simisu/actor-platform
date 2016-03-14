/*
 * Copyright (C) 2015 Actor LLC. <https://actor.im>
 */

package im.actor.runtime;

import im.actor.runtime.crypto.BlockCipher;
import im.actor.runtime.crypto.Digest;
import im.actor.runtime.crypto.primitives.digest.KeyDigest;
import im.actor.runtime.crypto.primitives.digest.MD5;
import im.actor.runtime.util.Hex;

public class Crypto {

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    private static final CryptoRuntime runtime = new CryptoRuntimeProvider();

    private static final RandomRuntime random = new RandomRuntimeProvider();

    public static void waitForCryptoLoaded() {
        runtime.waitForCryptoLoaded();
    }

    public static Digest createSHA256() {
        return runtime.SHA256();
    }

    public static BlockCipher createAES128(byte[] key) {
        return runtime.AES128(key);
    }

    public static byte[] MD5(byte[] data) {
        MD5 md5 = new MD5();
        md5.update(data, 0, data.length);
        byte[] res = new byte[16];
        md5.doFinal(res, 0);
        return res;
    }

    public static String keyHash(byte[] publicKey) {
        KeyDigest keyDigest = new KeyDigest();
        keyDigest.update(publicKey, 0, publicKey.length);
        byte[] res = new byte[8];
        keyDigest.doFinal(res, 0);
        return Hex.toHex(res);
    }

    /**
     * Calculating SHA256
     *
     * @param data source data
     * @return SHA256 of data
     */
    public static byte[] SHA256(byte[] data) {
        Digest sha256 = createSHA256();
        sha256.update(data, 0, data.length);
        byte[] res = new byte[32];
        sha256.doFinal(res, 0);
        return res;
    }

    /**
     * Generate securely random int
     *
     * @param maxValue maximum value of int
     * @return random value
     */
    public static int randomInt(int maxValue) {
        return random.randomInt(maxValue);
    }

    /**
     * Generate securely some amount of bytes
     *
     * @param len bytes count
     * @return random bytes
     */
    public static byte[] randomBytes(int len) {
        return random.randomBytes(len);
    }

    public static void nextBytes(byte[] data) {
        random.nextBytes(data);
    }

    /**
     * Calculating lowcase hex string
     *
     * @param bytes data for hex
     * @return hex string
     */
    public static String hex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static int fromHexShort(char a) {
        if (a >= '0' && a <= '9') {
            return a - '0';
        }
        if (a >= 'a' && a <= 'f') {
            return 10 + (a - 'a');
        }

        throw new RuntimeException();
    }

    public static byte[] fromHex(String hex) {
        byte[] res = new byte[hex.length() / 2];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((fromHexShort(hex.charAt(i * 2)) << 4) + fromHexShort(hex.charAt(i * 2 + 1)));
        }
        return res;
    }
}
