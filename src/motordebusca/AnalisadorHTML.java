package motordebusca;

import java.util.ArrayList;
import java.text.Normalizer;

public class AnalisadorHTML {
	
	public ArrayList<String> getLinksExistentes(String codigoHTML){
		ArrayList<String> arrayListLinks = new ArrayList<String>();
		int posicaoLinkEncontrado = 0;
		int posicaoFimLinkEncontrado;
		String linkEncontrado;

		
		while(codigoHTML.indexOf("href=\"http://", posicaoLinkEncontrado) != -1){
			posicaoLinkEncontrado = codigoHTML.indexOf("href=\"http://", posicaoLinkEncontrado) + 13;
			posicaoFimLinkEncontrado = codigoHTML.indexOf("/", posicaoLinkEncontrado);
			linkEncontrado = codigoHTML.substring(posicaoLinkEncontrado, posicaoFimLinkEncontrado);
			
			if(linkEncontrado.contains("\"")){
				posicaoFimLinkEncontrado = codigoHTML.indexOf("\"", posicaoLinkEncontrado);
				linkEncontrado = codigoHTML.substring(posicaoLinkEncontrado, posicaoFimLinkEncontrado);
			}
			
			if(linkEncontrado.contains("?")){
				posicaoFimLinkEncontrado = codigoHTML.indexOf("?", posicaoLinkEncontrado);
				linkEncontrado = codigoHTML.substring(posicaoLinkEncontrado, posicaoFimLinkEncontrado);
			}
			
			if(!arrayListLinks.contains(linkEncontrado)){
				arrayListLinks.add(linkEncontrado);			
			}
			posicaoLinkEncontrado = posicaoFimLinkEncontrado + 1;
		}
		
		return arrayListLinks;
	}
	public String getConteudo(String codigoHTML){
		String conteudo;
		
		conteudo = codigoHTML.substring(codigoHTML.indexOf("\n\n"));
		
		conteudo = conteudo.replaceAll("<script[^<]*</script>","");
		conteudo = conteudo.replaceAll("<style[^<]*</style>","");
		conteudo = conteudo.replaceAll("\\s+"," ");
		conteudo = conteudo.replaceAll("<[^>]*>","");
		conteudo = conteudo.replaceAll("&oacute;", "�");
		conteudo = conteudo.replaceAll("õ", "�");
		conteudo = conteudo.replaceAll("&nbsp;", " ");
		conteudo = conteudo.replaceAll("&eacute;", "�");
		conteudo = conteudo.replaceAll("&ecirc;", "�");
		conteudo = conteudo.replaceAll("�", "�");
		conteudo = conteudo.replaceAll("&atilde;", "�");
		conteudo = conteudo.replaceAll("ã", "�");
		conteudo = conteudo.replaceAll("&ccedil;", "�");
		conteudo = conteudo.replaceAll("ç", "�");
		conteudo = conteudo.replaceAll("�", "�");
		conteudo = conteudo.replaceAll("&middot;", ".");
		conteudo = conteudo.replaceAll("ú", "�");
		conteudo = conteudo.replaceAll("&aacute;", "�");
		conteudo = conteudo.replaceAll("&quot;", "\"");
		conteudo = conteudo.replaceAll("'", "");
		conteudo = conteudo.replace("{", " ");
		//a linha abaixo remove todos os caracteres especiais da string com o HTML da p�gina
		//conteudo = conteudo.replaceAll("[^[a-zA-Z�-�0-9]]", " ");
		
		return conteudo;
	}
	
}
