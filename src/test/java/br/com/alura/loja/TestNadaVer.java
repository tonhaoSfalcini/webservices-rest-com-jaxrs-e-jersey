package br.com.alura.loja;

import java.util.ArrayList;
import java.util.List;

public class TestNadaVer {

	@org.junit.Test
	public void testaFilaNfe(){
		List<Integer> pendencias = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			if(i%2 == 0) {
				pendencias.add(i);
				System.out.println(">> "+i+" > Emitida nota ");
			}
			
			if(i%15 == 0) {
				System.out.println("========> "+i+" > Capturar notas aptas para autorização");
				int count = 0;
				for(Integer nfe : pendencias) {
					if(nfe != 0 && i - nfe >= 30) {
						pendencias.set(pendencias.indexOf(nfe), null);
						count++;
					}
				}
				System.out.println("========> "+count+" notas retiradas.");
			}
		}
	}
	
}
