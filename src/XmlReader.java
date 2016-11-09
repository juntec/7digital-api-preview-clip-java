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
	protected Document doc=null;
	protected int tab;
	private String pretrackid=null;
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

		if(node.getNodeName().equals("track") &&
		   node.getChildNodes().item(2).getChildNodes().item(0).getFirstChild().getNodeValue().equals(art) &&
		   node.getChildNodes().item(0).getFirstChild().getNodeValue().equals(tit)){
			System.out.println("id = " + node.getAttributes().getNamedItem("id").getNodeValue());
			System.out.println("art= " + node.getChildNodes().item(2).getChildNodes().item(0).getFirstChild().getNodeValue());
			System.out.println("tit= " + node.getChildNodes().item(0).getFirstChild().getNodeValue());
			System.out.println("完全一致 ");
			trackid = node.getAttributes().getNamedItem("id").getNodeValue();
			return true;
		}

		return false;

	}

	/* 全ノードを探索 */
	public String walkThrough(String art , String tit){
		//		String trackID = null;

		if(doc != null){
			Node root=doc.getDocumentElement();
			recursiveWalk(root , art , tit);
			return trackid;
		}else{
			return null;
		}
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