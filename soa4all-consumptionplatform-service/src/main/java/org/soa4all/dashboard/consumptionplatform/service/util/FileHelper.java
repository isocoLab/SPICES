package org.soa4all.dashboard.consumptionplatform.service.util;

import java.io.*;

public class FileHelper
{

    public FileHelper()
    {
    }

    public String readFile(String file) throws Exception
    {
        String text;
        BufferedReader br;
        text = "";
        br = null;
        try {
			br = new BufferedReader(new FileReader((new File(file)).getCanonicalPath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        do
        {
            String aux = null;
			try {
				aux = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(aux == null)
            {
                break;
            }
            text = (new StringBuilder()).append(text).append(aux).append("<br>").toString();
        } while(true);
        try
        {
            if(br != null)
            {
                br.close();
            }
        }
        catch(IOException excep)
        {
            excep.printStackTrace();
            
        }
        
        try
        {
            if(br != null)
            {
                br.close();
            }
        }
        catch(IOException excep)
        {
            excep.printStackTrace();
           
        }
       
        try
        {
            if(br != null)
            {
                br.close();
            }
        }
        catch(IOException excep)
        {
            throw new Exception(excep.getMessage());
        }
        return text;
    }
}
