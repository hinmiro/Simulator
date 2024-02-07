package src.simu.model;

import src.simu.framework.ITapahtumanTyyppi;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum TapahtumanTyyppi implements ITapahtumanTyyppi{
	ARR1, Infotiski, UudenTilinAvaus, Talletus, SijoitusPalvelut;

}
