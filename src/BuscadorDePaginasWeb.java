import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import motordebusca.AnalisadorHTML;
import motordebusca.ClienteWeb;
import motordebusca.ManipuladorDeArquivo;

import database.GerenciadorBD;

public class BuscadorDePaginasWeb implements Runnable {

	
	private ManipuladorDeArquivo manipuladorDeArquivo;
	private AnalisadorHTML analisadorHTML;
	private ClienteWeb clienteWeb;
	private ArrayList<String> arrayListNovosLinks;
	private String urlParaAcessar;
	private String paginaHTML;
	private String ConteudoHTML;
	private String consultaSQL;
	private GerenciadorBD gbd;
	
	
	public BuscadorDePaginasWeb(){		
		manipuladorDeArquivo = new ManipuladorDeArquivo();
		analisadorHTML = new AnalisadorHTML();
		gbd = new GerenciadorBD();
	}
	
	
	public void run() {		
		
		do {
		
			try {
				manipuladorDeArquivo.verificarArquivosEdiretorios();
				urlParaAcessar = manipuladorDeArquivo.getProximoLinkParaAcessar();
				System.out.println("Conectando a: \"" + urlParaAcessar + "\"");
				
			} catch (IOException e) {
				System.out.println("Erro ao verificar arquivos e diretórios: " + e.getMessage());
			} catch (ClassNotFoundException e){
				System.out.println("Erro ao carregar links do arquivo: " + e.getMessage());
			}			
			
			if(urlParaAcessar != null){
			
				try {
					
					clienteWeb = new ClienteWeb(urlParaAcessar);
					
					if(clienteWeb.executarEobterRespostaDoServidor()){
						paginaHTML = clienteWeb.getRespostaDoServidor();

						manipuladorDeArquivo.salvarPaginaHTML(urlParaAcessar, paginaHTML);
						
						ConteudoHTML = analisadorHTML.getConteudo(paginaHTML);
						manipuladorDeArquivo.salvarPaginaHTML(urlParaAcessar + " conteudo", ConteudoHTML);	
						
						gbd.conectaBanco();
						
						gbd.salvaPalavrasBD(ConteudoHTML, urlParaAcessar);
						gbd.desconectaBanco();
						
						System.out.println("Página HTML salva em disco");
						
						arrayListNovosLinks = analisadorHTML.getLinksExistentes(paginaHTML);
						System.out.println("Novos links encontrados: " + arrayListNovosLinks.size());
						
						//for(int i=0; i<arrayListNovosLinks.size(); i++){
						//	System.out.println("link " + i + ": " + arrayListNovosLinks.get(i));
						//}
					}
					
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Erro ao separar mensagem HTTP das tags HTML: " + e.getMessage());
				} catch (UnknownHostException e) {
					System.out.println("Erro ao conectar a \"" + urlParaAcessar + "\": " + e.getMessage());
				} catch (IOException e) {
					System.out.println("Erro ao obter resposta do servidor web: " + e.getMessage());
				}
					
				
				try {
					manipuladorDeArquivo.setLink(urlParaAcessar, true);
					manipuladorDeArquivo.adicionarLinks(arrayListNovosLinks);
					
					//Teste
					manipuladorDeArquivo.adicionarLinks(arrayListNovosLinks);			
					ArrayList<String> todosOsLinksString = manipuladorDeArquivo.getTodosOsLinksString();
					//System.out.println("\n\nTodos os links armazenados\n");
					//for(int i=0; i<todosOsLinksString.size(); i++){
					//	System.out.println(todosOsLinksString.get(i));
					//}
					
					System.out.println("Qtde de links armazenados: " + todosOsLinksString.size() + "\n");
					
					
				} catch (FileNotFoundException e) {
					System.out.println("Erro ao salvar link: " + e.getMessage());
				} catch (IOException e) {
					System.out.println("Erro ao salvar arquivo: " + e.getMessage());
				} catch (NullPointerException e){
					System.out.println("Erro de ponteiro nulo: " + e.getMessage());
				}
				
				
				
			}
		
		} while (manipuladorDeArquivo.getProximoLinkParaAcessar() != null);
		
	}
	
	
	public static void main(String[] args) {
		Thread thread = new Thread(new BuscadorDePaginasWeb());
		thread.start();
	}

}
