package src.simu.model;

import src.simu.framework.*;
import src.eduni.distributions.*;

import java.util.Random;

public class OmaMoottori extends Moottori {

    private Saapumisprosessi saapumisprosessi;

    private Palvelupiste[] palvelupisteet;

    public OmaMoottori() {

        palvelupisteet = new Palvelupiste[4];

        palvelupisteet[0] = new Palvelupiste(new Normal(10, 6), tapahtumalista, TapahtumanTyyppi.INFOTISKI);
        palvelupisteet[1] = new Palvelupiste(new Normal(10, 10), tapahtumalista, TapahtumanTyyppi.UUDEN_TILIN_AVAUS);
        palvelupisteet[2] = new Palvelupiste(new Normal(5, 3), tapahtumalista, TapahtumanTyyppi.TALLETUS);
        palvelupisteet[3] = new Palvelupiste(new Normal(6, 9), tapahtumalista, TapahtumanTyyppi.SIJOITUS_PALVELUT);
        saapumisprosessi = new Saapumisprosessi(new Negexp(15, 5), tapahtumalista, TapahtumanTyyppi.SAAPUMINEN);

    }


    @Override
    protected void alustukset() {
        saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
    }

    @Override
    protected void suoritaTapahtuma(Tapahtuma t) {  // B-vaiheen tapahtumat

        Asiakas a;
        switch ((TapahtumanTyyppi) t.getTyyppi()) {

            case SAAPUMINEN:
                Asiakas as =  new Asiakas(generateTrueFalse());
                if (as.isOnVarattu())
                    palvelupisteet[1].lisaaJonoon(as);
                else
                    palvelupisteet[0].lisaaJonoon(as);
                saapumisprosessi.generoiSeuraava();
                break;
            case INFOTISKI:
                a = (Asiakas) palvelupisteet[0].otaJonosta();
                palvelupisteet[1].lisaaJonoon(a);
                break;
            case UUDEN_TILIN_AVAUS:
                if (palvelupisteet[1].onVarattuJonossa()){
                    a = (Asiakas) palvelupisteet[1].otaVarattuJonosta();
                    palvelupisteet[2].lisaaJonoon(a);
                }
                else {
                    a = (Asiakas) palvelupisteet[1].otaJonosta();
                    palvelupisteet[2].lisaaJonoon(a);
                }
                break;
            case TALLETUS:
                if (palvelupisteet[2].onVarattuJonossa()){
                    a = (Asiakas) palvelupisteet[2].otaVarattuJonosta();
                    palvelupisteet[3].lisaaJonoon(a);
                }
                else {
                    a = (Asiakas) palvelupisteet[2].otaJonosta();
                    palvelupisteet[3].lisaaJonoon(a);
                }
                break;
            case SIJOITUS_PALVELUT:
                if (palvelupisteet[3].onVarattuJonossa()){
                    a = (Asiakas) palvelupisteet[3].otaVarattuJonosta();
                }
                else {
                    a = (Asiakas) palvelupisteet[3].otaJonosta();
                }
                a.setPoistumisaika(Kello.getInstance().getAika());
                a.raportti();
        }
    }

    @Override
    protected void yritaCTapahtumat() {
        for (Palvelupiste p : palvelupisteet) {
            if (!p.onVarattu() && p.onJonossa()) {
                p.aloitaPalvelu();
            }
        }
    }

    @Override
    protected void tulokset() {
        System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());
        System.out.println("Keskimääräinen läpikulku aika on:  " + Asiakas.getAverageTimeSpent());
        System.out.println("Asiakkaita palveltu: " + Asiakas.getTotalCustomers());
        System.out.println("Keskimääräinen asiakastyytyväisyys: " + Asiakas.getHappyRating());
    }

    protected boolean generateTrueFalse(){
        Random random = new Random();
        double normalNum = new Normal(5, random.nextInt(10)+1).sample();
        return normalNum <= 2 || normalNum >= 8;
    }
}
