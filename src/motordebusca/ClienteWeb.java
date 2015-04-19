package motordebusca;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClienteWeb {
	
	ClienteHTTP clienteHTTP;
	ClienteHTTPS clienteHTTPS;
	
	String url;
	String diretorioDaPagina;
	String respostaDoServidor = "";
	String respostaHTTP = "";
	String paginaHTML = "";
	String paginaWeb;
	int codigoDeRespostaHTTP;
	String protocoloDeLocalizacao;
		
	
	public ClienteWeb(String url){
		this.url = url;
		clienteHTTP = new ClienteHTTP(url);
	}
	
	
	
	public boolean executarEobterRespostaDoServidor() throws IOException, UnknownHostException, StringIndexOutOfBoundsException {
							
		clienteHTTP.executar();	
		respostaHTTP = clienteHTTP.getRespostaHTTP();
		paginaHTML = clienteHTTP.getPaginaHTML();
		respostaDoServidor = respostaHTTP + "\n\n" + paginaHTML;
		codigoDeRespostaHTTP = clienteHTTP.getCodigoDeRespostaHTTP();
			
		//do {	
				
				if(codigoDeRespostaHTTP == 301 || codigoDeRespostaHTTP == 302){
					
					url = AnalisadorHTTP.getUrlDeNovaLocalizacao(respostaHTTP);
					diretorioDaPagina = AnalisadorHTTP.getDiretorioDeNovaLocalizacao(respostaHTTP);
										
					if(AnalisadorHTTP.getProtocoloDeNovaLocalizacao(respostaHTTP).equals("http")){
						clienteHTTP.setURL(url, diretorioDaPagina);
						//clienteHTTP = new ClienteHTTP(url, diretorioDaPagina);
						clienteHTTP.executar();
						respostaHTTP = clienteHTTP.getRespostaHTTP();
						paginaHTML = clienteHTTP.getPaginaHTML();
						codigoDeRespostaHTTP = clienteHTTP.getCodigoDeRespostaHTTP();
						respostaDoServidor = respostaHTTP + "\n\n" + paginaHTML;
						
					} else if(AnalisadorHTTP.getProtocoloDeNovaLocalizacao(respostaHTTP).equals("https")){
						clienteHTTPS = new ClienteHTTPS(url);												
						clienteHTTPS.executar();
						codigoDeRespostaHTTP = clienteHTTPS.getCodigoDeRespostaHTTP();
						//respostaHTTP = clienteHTTPS.getRespostaHTTP();
						paginaHTML = clienteHTTPS.getPaginaHTML();
						respostaDoServidor = paginaHTML;
						System.out.println("codigoDeRespostaHTTP: " + codigoDeRespostaHTTP);
						
					}
					//System.out.println("Protocolo: " + AnalisadorHTTP.getProtocoloDeNovaLocalizacao(respostaHTTP));
					//System.out.println("codigoDeRespostaHTTP: " + codigoDeRespostaHTTP);	
				}

		//} while (codigoDeRespostaHTTP != 200);		
			
		return true;
	}
	
	
	
	public String getRespostaDoServidor(){
		return respostaDoServidor;
	}
	
	
	
}
