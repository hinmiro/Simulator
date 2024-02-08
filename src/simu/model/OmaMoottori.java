package src.simu.model;

import src.simu.framework.*;
import src.eduni.distributions.*;

public class OmaMoottori extends Moottori {

    private Saapumisprosessi saapumisprosessi;

    private Palvelupiste[] palvelupisteet;

    public OmaMoottori() {

        palvelupisteet = new Palvelupiste[4];

        palvelupisteet[0] = new Palvelupiste(new Normal(10, 6), tapahtumalista, TapahtumanTyyppi.INFOTISKI);
        palvelupisteet[1] = new Palvelupiste(new Normal(10, 10), tapahtumalista, TapahtumanTyyppi.UUDEN_TILIN_AVAUS);
        palvelupisteet[2] = new Palvelupiste(new Normal(5, 3), tapahtumalista, TapahtumanTyyppi.TALLETUS);
        palvelupisteet[3] = new Palvelupiste(new Normal(6, 9), tapahtumalista, TapahtumanTyyppi.SIJOITUS_PALVELUT);
        saapumisprosessi = new Saapumisprosessi(new Negexp(15, 5), tapahtumalista, TapahtumanTyyppi.ARR1);

    }


    @Override
    protected void alustukset() {
        saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
    }

    @Override
    protected void suoritaTapahtuma(Tapahtuma t) {  // B-vaiheen tapahtumat

        Asiakas a;
        switch ((TapahtumanTyyppi) t.getTyyppi()) {

            case ARR1:
                palvelupisteet[0].lisaaJonoon(new Asiakas());
                saapumisprosessi.generoiSeuraava();
                break;
            case INFOTISKI:
                a = (Asiakas) palvelupisteet[0].otaJonosta();
                palvelupisteet[1].lisaaJonoon(a);
                break;
            case UUDEN_TILIN_AVAUS:
                a = (Asiakas) palvelupisteet[1].otaJonosta();
                palvelupisteet[2].lisaaJonoon(a);
                break;
            case TALLETUS:
                a = (Asiakas) palvelupisteet[2].otaJonosta();
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
    }


}
