package motordebusca;

import java.io.Serializable;

public class Link implements Serializable {
	
	String url;
	boolean visitado;
	
	public Link(String url, boolean estado){
		this.url = url;
		this.visitado = estado;
	}		
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isVisitado() {
		return visitado;
	}
	public void setVisitado(boolean estado) {
		this.visitado = estado;
	}
	
	public boolean equals(Object obj){
		Link link = (Link)obj;
		if(this.url.equals(link.getUrl())){
			return true;
		}
		return false;
	}	
	
}
