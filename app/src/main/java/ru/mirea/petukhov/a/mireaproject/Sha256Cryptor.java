package ru.mirea.petukhov.a.mireaproject;

import java.util.ArrayList;
import java.util.List;

public class Sha256Cryptor {
    private String inputString;
    private String inputBinary;
    private int inputBinaryLength;
    private int paddedInputBinaryLength;
    private String paddedInput;
    private List<SHA256Block> blocks;

    public Sha256Cryptor(String input) {
        this.inputString = input;
    }

    public String getHash() {
        this.inputBinary = getBinaryInput(this.inputString);
        this.inputBinaryLength = this.inputBinary.length();
        this.paddedInput = getPaddedInput(this.inputBinary);
        this.blocks = getBlocks(this.paddedInput);
        expandBlocks();
        compress();
        return buildResult();
    }

    private String buildResult(){
        StringBuilder blocksBinaryBuilder = new StringBuilder();
        StringBuilder resultBuilder = new StringBuilder();
        List<String> asciiCodes = new ArrayList<String>();

        for(SHA256Block block : this.blocks){
            blocksBinaryBuilder.append(block.toString());
        }

        for(int i = 0; i < blocksBinaryBuilder.length(); i += 4){
            StringBuilder codeBuilder = new StringBuilder();
            for(int j = i; j < i + 4; j++){
                codeBuilder.append(blocksBinaryBuilder.charAt(j));
            }
            asciiCodes.add(codeBuilder.toString());
        }

        for(String code : asciiCodes){
            resultBuilder.append((char) Integer.parseInt(code, 2));
        }

        return resultBuilder.toString();
    }

    private void compress(){
        for(SHA256Block block : this.blocks){
            block.compress();
        }
    }

    private void expandBlocks() {
        for(SHA256Block block : this.blocks){
            block.expand();
        }
    }

    public List<SHA256Block> getBlocks(String paddedInput) {
        List<SHA256Block> blocks = new ArrayList<>();
        for(int i = 0; i < paddedInput.length(); i += 512){
            List<String> words = new ArrayList<>();
            for(int j = i; j < i + 512; j+= 32) {
                StringBuilder wordBuilder = new StringBuilder();
                for(int k = j; k < j + 32; k++){
                    wordBuilder.append(paddedInput.charAt(k));
                }
                words.add(wordBuilder.toString());
            }
            blocks.add(new SHA256Block(words));
        }

        return blocks;
    }

    public String getPaddedInput(String inputBinary) {
        StringBuilder paddingBuilder = new StringBuilder(inputBinary);
        int paddingTo = getPaddingValue(inputBinary.length());
        paddingBuilder.append("1");

        for(int i = paddingBuilder.length(); i < paddingTo - 8; i++) {
            paddingBuilder.append("0");
        }

        paddingBuilder.append(getPaddedBinaryValue(Integer.toBinaryString(this.inputBinaryLength)));
        this.paddedInputBinaryLength = paddingBuilder.length();
        return paddingBuilder.toString();
    }

    public String getPaddedBinaryValue(String binary) {
        while(binary.length() < 8) {
            binary = "0" + binary;
        }
        return binary;
    }

    public int getPaddingValue(int length) {
        if(length <= 504){
            return 512;
        }
        if (length % 512 != 0) {
            return (length / 512) * 512 + 512;
        }
        return length + 512;
    }

    public String getBinaryInput(String text) {
        StringBuilder builder = new StringBuilder();
        for(char c : text.toCharArray()){
            String binaryForm = Integer.toBinaryString((int)c);
            while (binaryForm.length() < 8) {
                binaryForm = "0" + binaryForm;
            }
            builder.append(binaryForm);
        }
        return builder.toString();
    }
}
