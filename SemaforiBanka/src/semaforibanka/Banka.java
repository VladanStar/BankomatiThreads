/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforibanka;

import java.util.concurrent.Semaphore;


public class Banka {
    
    static Semaphore bankomati = new Semaphore(4);
    
    static class KorisnikBankomata extends Thread {
        
        private String ime;
        
        KorisnikBankomata(String ime)
        {
            this.ime = ime;
        }

        @Override
        public void run() {
            try{
                System.out.println(this.ime + " trazi dozvolu");
                System.out.println(this.ime + " trenutni broj slobodnih bankomata " + bankomati.availablePermits());
            
                bankomati.acquire();
                //kriticna sekcija
                System.out.println(this.ime + " dobio dozvolu i prilazi bankomatu");
                
                try {
                    for (int i = 1; i <= 3; i++){
                        System.out.println(this.ime + " obavlja operaciju " + i +
                                ", trenutno slobodno " + bankomati.availablePermits() + " bankomata");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e){
                    System.out.println("Doslo je do greske u radu sa bankomatom");
                } finally {
                    //moramo da oslobodimo resurs
                    System.out.println(this.ime + " oslobadja bankomat ");
                    bankomati.release();
                    System.out.println(this.ime + " trenutni broj slobodnih bankomata: " + bankomati.availablePermits());
                }
                
            } catch (InterruptedException e){
                System.out.println("Izvinite, bankomati nisu dostupni");
            }
        }  
        
    }
    
    public static void main (String[] args){
        System.out.println("Ukupan broj dostupnih bankomata : " + bankomati.availablePermits());
        for (int i = 1; i <= 10; i++){
            KorisnikBankomata k = new KorisnikBankomata("Korisnik " + i);
            k.start();
        }
    }
}
