/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.polynom;
import com.mathpar.number.Element;
import com.mathpar.number.NumberZ;
import com.mathpar.number.Ring;
import java.util.*;
import com.mathpar.number.NumberZp32;
/**
 *
 * @author gena
 */
public class Zhegalkin extends Polynom implements Cloneable, java.io.Serializable{
   static Ring ringZh = Ring.ringZp32X; 
   static{ ringZh.MOD32=2L;}
   
   public Zhegalkin(int[] powers, int coeffNumber){
            if (powers.length % coeffNumber ==0) { coeffs=new NumberZp32[coeffNumber];
                for (int i = 0; i < coeffNumber; i++) coeffs[i]=ringZh.numberONE;}
             else this.coeffs=new NumberZp32[0];
        this.powers=powers;
   }
   
   public Zhegalkin(int[] powers, NumberZp32[] coeffs){
        this.coeffs=coeffs;
        this.powers=powers;
   }
    public Zhegalkin(){};
    public Zhegalkin(Polynom p){coeffs=p.coeffs; powers=p.powers; }
    public Zhegalkin(Polynom p, Ring r){coeffs=p.coeffs; powers=p.powers; }
    public Zhegalkin(int[] randomType, Random rnd ) {
        Polynom Pol=random(randomType, rnd , ringZh);
        this.coeffs=Pol.coeffs;  this.powers=Pol.powers;     }
    
    @Override
    public Element add(Element b, Ring ring){
        Element res=((Polynom)this).add(b, ring);
        if(res instanceof Polynom ) return new Zhegalkin((Polynom)res);
        return res;
    }
        @Override
    public Element subtract(Element b, Ring ring){
        Element res=((Polynom)this).subtract(b, ring);
        if(res instanceof Polynom ) return new Zhegalkin((Polynom)res);
        return res;
    }
            @Override
    public Element multiply(Element b, Ring ring){
        Element res=((Polynom)this).multiply(b, ring);
        if(res instanceof Polynom ) return new Zhegalkin((Polynom)res);
        return res;
    }
    public Zhegalkin add(Polynom b){
       return   new Zhegalkin(((Polynom)this).add((Polynom)b, ringZh));
    }
    /**
     * 
     * @param b
     * @return 
     */
   public Zhegalkin multiply(Zhegalkin b){
       System.out.println("THIS="+this);
       System.out.println("b="+b);
      return   new Zhegalkin(((Polynom)this).multiply((Polynom)b, ringZh));
    
   }
   
   public Zhegalkin subtract (Polynom b){      
      return   new Zhegalkin(((Polynom)this).subtract((Polynom)b, ringZh)); 
    }
}
