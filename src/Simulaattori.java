package src;

import src.simu.framework.Moottori;
import src.simu.framework.Trace;
import src.simu.framework.Trace.Level;
import src.simu.model.OmaMoottori;

public class Simulaattori { //Text based simulation

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);
		Moottori m = new OmaMoottori();
		m.setSimulointiaika(1050);
		m.aja();
	}
}
