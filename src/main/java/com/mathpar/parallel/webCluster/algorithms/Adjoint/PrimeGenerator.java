/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mathpar.parallel.webCluster.algorithms.Adjoint;

import java.util.ArrayList;

/**
 *
 * @author derbist
 */
public class PrimeGenerator {
    
    int []basicSieve;
    int basicSieveSize,maxNumberForSearch;
    
    public PrimeGenerator(int maxNumberForSearch){
        this.maxNumberForSearch=maxNumberForSearch;
        int sqrtFromLast=(int)Math.floor(Math.sqrt((double)maxNumberForSearch));
        boolean []basicSieveTmp=new boolean[sqrtFromLast+1];
        int sqrtFromBasicBorder=(int)Math.floor(Math.sqrt((double)sqrtFromLast));
        for (int i=2; i<=sqrtFromBasicBorder; i++){
            if (!basicSieveTmp[i]){
                for (int j=i*i; j<=sqrtFromLast; j+=i){
                    basicSieveTmp[j]=true;
                }                
            }
        }
        for (int i=2; i<=sqrtFromLast; i++){
            if (!basicSieveTmp[i]){
                basicSieveSize++;
            }
        }
        basicSieve=new int[basicSieveSize];
        basicSieveSize=0;
        for (int i=2; i<=sqrtFromLast; i++){
            if (!basicSieveTmp[i]){
                basicSieve[basicSieveSize++]=i;
            }
        }
    }
    
    void printBasic(){
        for (int i=0; i<basicSieveSize; i++){
            System.out.println(basicSieve[i]+" ");
        }
    }
    
    ArrayList<Integer> generatePrimes(int firstNumber, int lastNumber) throws Exception {
        if (lastNumber>maxNumberForSearch || lastNumber<firstNumber){
            throw new Exception("Wrong max number for search!");
        }
        int rangeLen=lastNumber-firstNumber+1;
        boolean []sieve=new boolean[rangeLen];
        for (int i=0; i<basicSieveSize; i++){
            int curPrime=basicSieve[i];
            int firstNumbForScan=(firstNumber/curPrime)*curPrime;
            if (firstNumber%curPrime!=0){
                firstNumbForScan+=curPrime;
            }
            if (firstNumbForScan==curPrime){
                firstNumbForScan*=2;
            }
            for (int j=firstNumbForScan; j<=lastNumber&&j>=0; j+=curPrime){         
                sieve[j-firstNumber]=true;
            }
        }
        ArrayList<Integer> res=new ArrayList<Integer>();
        for (int i=0; i<rangeLen; i++){
            if (!sieve[i]){
                res.add(i+firstNumber);
            }
        }
        return res;
    }
    
    int[] generatePrimesIntVer(int firstNumber, int lastNumber) throws Exception {
        if (lastNumber>maxNumberForSearch || lastNumber<firstNumber){
            throw new Exception("Wrong max number for search!");
        }
        int rangeLen=lastNumber-firstNumber+1;
        boolean []sieve=new boolean[rangeLen];
        for (int i=0; i<basicSieveSize; i++){
            int curPrime=basicSieve[i];
            int firstNumbForScan=(firstNumber/curPrime)*curPrime;
            if (firstNumber%curPrime!=0){
                firstNumbForScan+=curPrime;
            }
            if (firstNumbForScan==curPrime){
                firstNumbForScan*=2;
            }
            for (int j=firstNumbForScan; j<=lastNumber&&j>=0; j+=curPrime){         
                sieve[j-firstNumber]=true;
            }
        }
        int primesSize = 0;
        for(int i = 0 ; i<sieve.length; i++){
            if(!sieve[i]) primesSize++;
        }
       int[] res=new int[primesSize];
        int k = 0;
        for (int i=0; i<rangeLen; i++){
            if (!sieve[i]){
                res[k] = i +firstNumber;
                k++;
            }
        }
        return res;
    }
}