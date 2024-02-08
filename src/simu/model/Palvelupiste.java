package src.simu.model;

import src.simu.framework.*;

import java.util.LinkedList;
import java.util.PriorityQueue;

import src.eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class Palvelupiste {

    private final LinkedList<Asiakas> jono = new LinkedList<>(); // Tietorakennetoteutus
    private final LinkedList<Asiakas> varattuJono = new LinkedList<>();
    private final ContinuousGenerator generator;
    private final Tapahtumalista tapahtumalista;
    private final TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;

    //JonoStartegia strategia; //optio: asiakkaiden järjestys

    private boolean varattu = false;


    public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi) {
        this.tapahtumalista = tapahtumalista;
        this.generator = generator;
        this.skeduloitavanTapahtumanTyyppi = tyyppi;

    }

    //Asiakas(boolean varattuAika)
    public void lisaaJonoon(Asiakas a) {   // Jonon 1. asiakas aina palvelussa
        if (a.isOnVarattu())
            varattuJono.add(a);
        else jono.add(a);
    }

    public Asiakas otaJonosta() {  // Poistetaan palvelussa ollut
        return jono.poll();
    }

    public Asiakas otaVarattuJonosta() {
        return varattuJono.poll();
    }


    public void aloitaPalvelu() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana

        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId() + " reservation: " + jono.peek().isOnVarattu());

        varattu = true;
        double palveluaika = generator.sample();
        tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
    }


    public boolean onVarattu() {
        return varattu;
    }


    public boolean onJonossa() {
        return !jono.isEmpty();
    }
    public boolean onVarattuJonossa() {
        return !varattuJono.isEmpty();
    }

}
