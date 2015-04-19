package motordebusca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClienteHTTP {
	
	Socket socket;
	BufferedReader bufferedReader;
	PrintStream printStream;
	
	String url;
	int porta = 80;
	String metodoHTTP = "GET";
	String diretorioDaPagina = "/";
	String versaoHTTP = "1.0";
	String requisicaoHTTP;
	
	String respostaDoServidor = "";
	String respostaHTTP = "";
	String paginaHTML = "";
	int codigoDeRespostaHTTP = 0;
	
	
	public ClienteHTTP(String url){
		this.url = url;
		construirRequisicaoHTTP();
	}
	
	
	public ClienteHTTP(String url, String diretorioDaPagina){
		this.url = url;
		this.diretorioDaPagina = diretorioDaPagina;
		construirRequisicaoHTTP();
	}
	
	
	public void executar() throws IOException, UnknownHostException, StringIndexOutOfBoundsException {
		construirRequisicaoHTTP();
		conectar();
		enviarRequisicaoHTTP();
		obterRespostaDoServidor();
		separarRepostaHTTPdaPaginaHTML();
		
		codigoDeRespostaHTTP = AnalisadorHTTP.getCodigoDeRespostaHTTP(respostaHTTP);
		//System.out.println("\n----- Requisicao HTTP -----\n" + requisicaoHTTP + "\n");
		//System.out.println("\n----- Resposta HTTP ------\n" + respostaHTTP + "\n");
		//System.out.println("\n----- codigoDeRespostaHTTP: " + codigoDeRespostaHTTP + "\n");
		
		finalizarConexao();
	}
	
	
	private void conectar() throws UnknownHostException, IOException {
		socket = new Socket(url, porta);
		printStream = new PrintStream(socket.getOutputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));		
	}
	
	
	private void construirRequisicaoHTTP(){
		requisicaoHTTP = metodoHTTP + " " + diretorioDaPagina + " HTTP/" + versaoHTTP + "\n";
		requisicaoHTTP = requisicaoHTTP + "Host: " + url + "\n";
		requisicaoHTTP = requisicaoHTTP + "Connection: close" + "\n";
		requisicaoHTTP = requisicaoHTTP + "User-Agent: Mozilla/5.0" + "\n\n";
	}
	
	
	private void enviarRequisicaoHTTP(){		
		printStream.print(requisicaoHTTP);		
	}
	
	
	private void obterRespostaDoServidor() throws IOException {
		String linha;
		respostaDoServidor = "";
		while ((linha = bufferedReader.readLine()) != null){
			respostaDoServidor = respostaDoServidor + linha + "\n";
		}		
	}
	
	
	private void separarRepostaHTTPdaPaginaHTML() throws StringIndexOutOfBoundsException {				
		int posicaoLinhaEmBranco = respostaDoServidor.indexOf("\n\n");
		respostaHTTP = respostaDoServidor.substring(0, posicaoLinhaEmBranco);
		paginaHTML = respostaDoServidor.substring(posicaoLinhaEmBranco+2);
	}
	
	
	public void finalizarConexao() throws IOException{
		if(bufferedReader != null){
			bufferedReader.close();
		}
		
		if(printStream != null){
			printStream.close();
		}
		
		if(socket != null || socket.isConnected()){
			socket.close();
		}
	}
	
	
	public String getPaginaHTML(){
		return paginaHTML;
	}
	
	
	public String getRespostaHTTP(){
		return respostaHTTP;
	}
	
	
	
	public String getRespostaDoServidor(){
		return respostaDoServidor;
	}
	
	
	public int getCodigoDeRespostaHTTP(){
		return codigoDeRespostaHTTP;
	}
	
	
	public void setURL(String url){
		this.url = url;
		construirRequisicaoHTTP();
	}
	
	
	public void setURL(String url, String diretorio){
		this.url = url;
		this.diretorioDaPagina = diretorio;
		construirRequisicaoHTTP();
	}
	
	
	public void setDiretorioDaPagina(String diretorio){
		this.diretorioDaPagina = diretorio;
	}
		
		
	

}
