import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlReader{
	protected Document doc;
	protected int tab;
	private String trackid=null;
	private boolean compcheck=false;
	
	public XmlReader(String filename){
		try{
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			/* XML文書を読み込む.*/
			doc=db.parse(new FileInputStream(filename));
		}catch(Exception e){
			e.printStackTrace();
		}
		tab=0;
	}

	public boolean getNodeInfo(Node node, String art , String tit){
		
		/* ノードの種類を出力 */
		//System.out.println(node.getNodeType());
		//tabbing();
		/* ノード名を出力 */
		if(node.getNodeName() != null &&  !node.getNodeName().equals("#text")  ){		
			if(node.getAttributes() != null && node.getAttributes().getNamedItem("id") != null && node.getNodeName().equals("track") && node.getNodeName().equals("track") ){
				System.out.println(node.getAttributes().getNamedItem("id").getNodeValue() + " <------ track id");
				trackid = node.getAttributes().getNamedItem("id").getNodeValue();
			}else{
			}
		}
	
		if(node.getParentNode().getNodeName().equals("title") && node.getParentNode().getParentNode().getNodeName().equals("track")){

			if(node.getNodeValue() != null){
				//tabbing();
				if(!node.getNodeValue().equals(tit))
					trackid = null;
				System.out.println(node.getNodeValue() + " <------- title " + trackid);
			}
		}else if(node.getParentNode().getNodeName().equals("name") && node.getParentNode().getParentNode().getNodeName().equals("artist")
				 &&  node.getParentNode().getParentNode().getParentNode().getNodeName().equals("track")){

			if(node.getNodeValue() != null){
				
				System.out.println(node.getNodeValue() + " <------- artist" + trackid);
				if(!node.getNodeValue().equals(art) ){
					trackid = null;
					return false;
				}else{
					System.out.println("完全一致 " + art + " " + tit + " " + trackid);
					return true;
				}
			
			}
		}else{
			if(node.getNodeValue() != null){
				//tabbing();
				//System.out.println(node.getNodeValue());
			}
		}
		
		return false;
		
	}

	/* 全ノードを探索 */
	public String walkThrough(String art , String tit){
//		String trackID = null;
		
		Node root=doc.getDocumentElement();
		recursiveWalk(root , art , tit);
		
		return trackid;
	}

	private void recursiveWalk(Node node , String art , String tit){
		/*
これは,XML文書のインデントなどの空白のノードを読み飛ばすための処理.
Node.TEXT_NODE ノードがテキストで,ノードの値の空白を除いた文字列の長さが0の場合は読み飛ばす.
		 */

		if(node.getNodeType()==Node.TEXT_NODE && node.getNodeValue().trim().length()==0){
			return;
		}
		getNodeInfo(node , art , tit);
		tab++;
		/* node.getFirstChild : nodeの最初の子を得る */
		/* child.getNextSibling : childの兄弟ノードを得る */
		for(Node child=node.getFirstChild();child!=null;child=child.getNextSibling()){
			recursiveWalk(child , art , tit);
		}
		tab--;
	}

	protected void tabbing(){
		for(int i=0;i<tab;i++){
			System.out.print("\t");
		}
	}


}