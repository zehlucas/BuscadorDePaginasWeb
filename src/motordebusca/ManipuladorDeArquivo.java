package motordebusca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class ManipuladorDeArquivo {
	
	File fileLinks;
	File fileDiretorioPaginasWeb;
	File filePaginaHTML;
	BufferedWriter bufferedWriter;
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	
	String nomeArquivoLinks = "links.lk";
	String diretorioPaginasWeb = "paginasWeb";
	
	boolean comCopiaTXT = true;
	String nomeArquivoLinksTXT = "links.txt";
	File fileLinksTXT;
	
	ArrayList<Link> arrayListLinks = new ArrayList<Link>();
	
	public ManipuladorDeArquivo(){		
		fileLinks = new File(nomeArquivoLinks);
		fileDiretorioPaginasWeb = new File(diretorioPaginasWeb);
		
		fileLinksTXT = new File(nomeArquivoLinksTXT);
	}
	
	
	public void verificarArquivosEdiretorios() throws IOException, ClassNotFoundException {		
			if(fileLinks.exists()){
				carregarLinksDoArquivo();
			} else {
				fileLinks.createNewFile();
				arrayListLinks.add(new Link("www.globo.com", false));
				salvarLinksNoArquivo();
			}
			
			if(!fileDiretorioPaginasWeb.exists()){
				fileDiretorioPaginasWeb.mkdir();
			}				
	}
	
	
	public String getProximoLinkParaAcessar(){			
		Link link;
		for(int i=0; i<arrayListLinks.size(); i++){
			link = arrayListLinks.get(i);
			if(!link.isVisitado()){
				return link.getUrl();
			}
		}	 
		return null;
	}
	
	
	
	public void salvarPaginaHTML(String nome, String conteudoHTML) throws IOException {
		filePaginaHTML = new File(diretorioPaginasWeb + "/" + nome + ".html");
		
			if(!filePaginaHTML.exists()){
				filePaginaHTML.createNewFile();
				bufferedWriter = new BufferedWriter(new FileWriter(filePaginaHTML));
				bufferedWriter.write(conteudoHTML);
				bufferedWriter.close();
			}
			
	}
	
	
	
	public void setLink(String url, boolean estado) throws FileNotFoundException, IOException {
		Link link = new Link(url, estado);
		int posicaoLinkEncontrado;
		
		if((posicaoLinkEncontrado=arrayListLinks.indexOf(link)) != -1){
			Link linkEncontrado = arrayListLinks.get(posicaoLinkEncontrado);
			linkEncontrado.setVisitado(estado);
			 
			salvarLinksNoArquivo();			 
		}
	}
	
	
	public void adicionarLinks(ArrayList<String> novosLinks) throws FileNotFoundException, IOException {
		Link novoLink;
		for(int i=0; i<novosLinks.size(); i++){
			novoLink = new Link(novosLinks.get(i), false);
			if(!arrayListLinks.contains(novoLink)){
				arrayListLinks.add(new Link(novosLinks.get(i), false));
			}
		}
		salvarLinksNoArquivo();
	}
	
	
	private void salvarLinksNoArquivo() throws FileNotFoundException, IOException {
		objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileLinks));
		objectOutputStream.writeObject(arrayListLinks);
		objectOutputStream.close();
		
		if(comCopiaTXT){
			salvarLinksNoArquivoTXT();
		}
	}
	
	
	private void salvarLinksNoArquivoTXT() throws FileNotFoundException, IOException{
		PrintStream printStream = new PrintStream(fileLinksTXT);
		String linkString;
		Link link;
		
		for(int i=0; i<arrayListLinks.size(); i++){
			link = arrayListLinks.get(i);
			linkString = link.getUrl() + " - " + link.isVisitado();
			printStream.println(linkString);
		}
		
		printStream.close();
	}
	
	
	private void carregarLinksDoArquivo() throws FileNotFoundException, IOException, ClassNotFoundException{
		objectInputStream = new ObjectInputStream(new FileInputStream(fileLinks));
		arrayListLinks = (ArrayList<Link>) objectInputStream.readObject();
		objectInputStream.close();
	}
	
	
	public ArrayList<String> getTodosOsLinksString(){
		ArrayList<String> todosOsLinksString = new ArrayList<String>();
		for(int i=0; i<arrayListLinks.size(); i++){
			todosOsLinksString.add(arrayListLinks.get(i).getUrl() + " - " + arrayListLinks.get(i).isVisitado());
		}
		return todosOsLinksString;
	}
	
	
	public ArrayList<Link> getTodosOsLinks(){
		return arrayListLinks;
	}	
	

}
