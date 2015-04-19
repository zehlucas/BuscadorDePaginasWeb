package motordebusca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
 
public class ClienteHTTPS{
	
	HttpsURLConnection con;
	URL url;
	
	String https_url = "";
	String protocolo = "https://";
	BufferedReader bufferedReader;
	String paginaHTML = "";
	
	
	
	public ClienteHTTPS(String url){
		this.https_url = protocolo + url;
		System.out.println("https_url: " + https_url);
	}
	
 
	public void executar() throws MalformedURLException, IOException {
 		 
		conectar();
		obterPaginaHTML();
 
		//dumpl all cert info
		//print_https_cert(con);
 
		//dump all the content
		//print_content(con);	    
 
	}
	
	
	private void conectar() throws MalformedURLException, IOException {
		url = new URL(https_url);
		con = (HttpsURLConnection)url.openConnection();
		bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		System.out.println("Conectado a: " + https_url);
	}
 
	
	private void obterPaginaHTML() throws IOException {		
		String linha;
		paginaHTML = "";
		while ((linha = bufferedReader.readLine()) != null){
			paginaHTML = paginaHTML + linha + "\n";
		}
		bufferedReader.close();
	}
	
	
	public String getPaginaHTML(){
		return paginaHTML;
	}
	
	
	public void setURL(String url){
		this.https_url = url;
	}
	
	
	public int getCodigoDeRespostaHTTP() throws IOException{
		if(con != null){
			return con.getResponseCode();
		}
		return -1;
	}
 
}
