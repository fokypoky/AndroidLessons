package ru.mirea.petukhov.a.mireaproject;

import java.util.List;

public class SHA256Block {
    private String a, b, c, d, e, f, g, h;
    public static final int[] H = {0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19};
    public static final int[] K = {
            0x428a2f98,0x71374491,0xb5c0fbcf,0xe9b5dba5,
            0x3956c25b,0x59f111f1,0x923f82a4,0xab1c5ed5,
            0xd807aa98,0x12835b01,0x243185be,0x550c7dc3,
            0x72be5d74,0x80deb1fe,0x9bdc06a7,0xc19bf174,
            0xe49b69c1,0xefbe4786,0x0fc19dc6,0x240ca1cc,
            0x2de92c6f,0x4a7484aa,0x5cb0a9dc,0x76f988da,
            0x983e5152,0xa831c66d,0xb00327c8,0xbf597fc7,
            0xc6e00bf3,0xd5a79147,0x06ca6351,0x14292967,
            0x27b70a85,0x2e1b2138,0x4d2c6dfc,0x53380d13,
            0x650a7354,0x766a0abb,0x81c2c92e,0x92722c85,
            0xa2bfe8a1,0xa81a664b,0xc24b8b70,0xc76c51a3,
            0xd192e819,0xd6990624,0xf40e3585,0x106aa070,
            0x19a4c116,0x1e376c08,0x2748774c,0x34b0bcb5,
            0x391c0cb3,0x4ed8aa4a,0x5b9cca4f,0x682e6ff3,
            0x748f82ee,0x78a5636f,0x84c87814,0x8cc70208,
            0x90befffa,0xa4506ceb,0xbef9a3f7,0xc67178f2
    };

    public String[] words = new String[64];
    public SHA256Block(List<String> words) {
        for(int i = 0; i < words.size(); i++) {
            this.words[i] = words.get(i);
        }
        for(int i = 16; i < 64; i++){
            this.words[i] = "00000000000000000000000000000000";
        }

        a = get32PaddedValue(Integer.toBinaryString(H[0]));
        b = get32PaddedValue(Integer.toBinaryString(H[1]));
        c = get32PaddedValue(Integer.toBinaryString(H[2]));
        d = get32PaddedValue(Integer.toBinaryString(H[3]));
        e = get32PaddedValue(Integer.toBinaryString(H[4]));
        f = get32PaddedValue(Integer.toBinaryString(H[5]));
        g = get32PaddedValue(Integer.toBinaryString(H[6]));
        h = get32PaddedValue(Integer.toBinaryString(H[7]));
    }
    public void expand() {
        // s0 = s0 = (w[i-15] rightrotate 7) xor (w[i-15] rightrotate 18) xor (w[i-15] rightshift 3)
        // s1 = (w[i- 2] rightrotate 17) xor (w[i- 2] rightrotate 19) xor (w[i- 2] rightshift 10)
        // w[i] = w[i-16] + s0 + w[i-7] + s1
        for(int i = 16; i < words.length; i++){
            String s0 = xor(xor(rotr(words[i - 15], 7), rotr(words[i - 15], 18)), rightShift(words[i - 15], 3));
            String s1 = xor(xor(rotr(words[i - 2], 17), rotr(words[i - 2], 19)), rightShift(words[i - 2], 10));
            words[i] = getBinarySum(getBinarySum(words[i - 16], s0) , getBinarySum(words[i - 7], s1));
        }
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append(a);
        resultBuilder.append(b);
        resultBuilder.append(c);
        resultBuilder.append(d);
        resultBuilder.append(e);
        resultBuilder.append(f);
        resultBuilder.append(g);
        resultBuilder.append(h);

        return resultBuilder.toString();
    }
    public void compress(){
//        S1 = (e rightrotate 6) xor (e rightrotate 11) xor (e rightrotate 25)
//        ch = (e and f) xor ((not e) and g)
//        temp1 = h + S1 + ch + k[i] + w[i]
//        S0 = (a rightrotate 2) xor (a rightrotate 13) xor (a rightrotate 22)
//        maj = (a and b) xor (a and c) xor (b and c)
//        temp2 := S0 + maj
//        h = g
//        g = f
//        e = d + temp1
//        d = c
//        c = b
//        b = a
//        a = temp1 + temp2
        for(int i = 0; i < 63; i++){
            String s1 = xor(xor(rotr(e, 6), rotr(e, 11)) , rotr(e, 25));
            String ch = xor(and(e, f), and(not(e), g));
            String temp1 = getBinarySum(words[i], getBinarySum(get32PaddedValue(Integer.toBinaryString(K[i])), getBinarySum(ch, getBinarySum(s1, h))));
            String s0 = xor(xor(rotr(a, 2), rotr(a, 13)) , rotr(a, 22));
            String maj = xor(xor(and(a, b), and(a, c)) , and(b, c));
            String temp2 = getBinarySum(s0, maj);
            h = g;
            g = f;
            e = getBinarySum(d, temp1);
            d = c;
            c = b;
            b = a;
            a = getBinarySum(temp1, temp2);
        }
    }
    public String getBinarySum(String x, String y){
        long xlong = Long.parseLong(x, 2);
        long ylong = Long.parseLong(y, 2);
        long result = (xlong + ylong) % (long)Math.pow(2, 32);
        return get32PaddedValue(Long.toBinaryString(result));
    }
    public String and(String x, String y) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == '1' && y.charAt(i) == '1') {
                result.append("1");
            } else {
                result.append("0");
            }
        }
        return get32PaddedValue(result.toString());
    }
    public String get32PaddedValue(String value){
        while(value.length() < 32){
            value = "0" + value;
        }
        return value;
    }

    public String xor(String x, String y){
        StringBuilder xorBuilder = new StringBuilder();

        for(int i = 0; i < x.length(); i++){
            if(x.charAt(i) != y.charAt(i)){
                xorBuilder.append("1");
                continue;
            }
            xorBuilder.append("0");
        }

        return get32PaddedValue(xorBuilder.toString());
    }

    public String not(String value) {
        StringBuilder notBuilder = new StringBuilder();

        for(int i = 0; i < value.length(); i++){
            if(value.charAt(i) == '1'){
                notBuilder.append('0');
                continue;
            }
            notBuilder.append('1');
        }

        return notBuilder.toString();
    }

    public String rightShift(String input, int n) {
        StringBuilder shiftBuilder = new StringBuilder();

        for(int i = 0; i < n; i++){
            shiftBuilder.append("0");
        }

        for(int i = 0; i < input.length() - n; i++){
            shiftBuilder.append(input.charAt(i));
        }

        return get32PaddedValue(shiftBuilder.toString());
    }

    public String rotr(String bitsInput, int n) {
        char[] bits = bitsInput.toCharArray();

        n = n % bits.length;
        bits = reverse(bits, 0, bits.length - 1);
        bits = reverse(bits, 0, n - 1);
        bits = reverse(bits, n, bits.length - 1);

        StringBuilder builder = new StringBuilder();
        for(char c : bits){
            builder.append(c);
        }
        return get32PaddedValue(builder.toString());
    }

    private char[] reverse(char[] arr, int start, int end) {
        while (start < end) {
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
        return arr;
    }
}
