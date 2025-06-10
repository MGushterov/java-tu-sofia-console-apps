package saa_2;

import java.util.Scanner;
import java.lang.StringBuilder;

public class Main {
    public static void main(String[] args) {
        Binary binary = new Binary();
        System.out.println(binary.getBinary(13));
    }
}

class Matrix {
    private int[][] matrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getDimension() {
        Scanner scanner = new Scanner(System.in);
        int dimension = scanner.nextInt();
        return dimension;
    }

    public void fillMatrix(int dimension) {
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                this.matrix[i][j] = scanner.nextInt();
            }
        }
    }

    public void printGreaterSum(int dimension) {
        int sumOfElementsOnSecondaryDiagonal = 0;
        int sumOfAmountOfElementsBelowMainDiagonal = 0;
        for(int row = 0; row < dimension; row++) {
            for(int col = 0; col < dimension; col++) {
                if(row == dimension - col - 1) {
                    if(this.matrix[row][col] % 2 == 0) {
                        sumOfElementsOnSecondaryDiagonal += this.matrix[row][col];
                    }
                }
                if(row > col) {
                    int sumOfIndices = row + col;
                    if(this.matrix[row][col] > sumOfIndices) {
                        sumOfAmountOfElementsBelowMainDiagonal += this.matrix[row][col];
                    }
                }
            }
        }
        if(sumOfElementsOnSecondaryDiagonal > sumOfAmountOfElementsBelowMainDiagonal) {
            System.out.println();
        }
        else {
            System.out.println();
        }
    }
}

class Binary {
    public String getBinary(int decimal) {
        if(decimal == 0) {
            return "0";
        }
        if(decimal == 1) {
            return "1";
        }

        return getBinary(decimal / 2) + (decimal % 2);
    }
}