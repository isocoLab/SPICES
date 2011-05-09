package org.soa4all.dashboard.gwt.module.consumptionplatform.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {

    public String readFile(String file) {
	String text = "";
	BufferedReader br = null;
	try {
	    br = new BufferedReader(new FileReader(new File(file).getCanonicalPath()));
	    while (true) {
		String aux = br.readLine(); // Se lee la linea
		if (aux == null)
		    break; // Se verifica si se ha terminado el archivo
		text = text + aux + "<br>";
	    }
	} catch (IOException ex) {
	    ex.printStackTrace();
	} finally {
	    try {
		if (br != null)
		    br.close();
	    } catch (IOException excep) {
		excep.printStackTrace();
	    }
	}
	return text;
    }
}
