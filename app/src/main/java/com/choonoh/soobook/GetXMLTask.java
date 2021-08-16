package com.choonoh.soobook;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GetXMLTask extends AsyncTask<String, Void, Document> {

    private TextView textview;
    private Context ctx;

    public GetXMLTask (Context ctx, TextView textview){
        this.ctx = ctx;
        this.textview = textview ;
    }

    @Override
    protected Document doInBackground(String... urls){
        URL url;
        Document doc = null;

        try{
            url = new URL("http://book.interpark.com/api/bestSeller.api?key=D10E38E11FF9AF9A94BBFCEA6E7C69EB862A51DD8A9A6F6F0141AA42540FEF41&categoryId=100");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();


        } catch (Exception e){

        }

        return doc;

    }

    @Override
    protected void onPostExecute(Document doc){

        String s = "";
        NodeList nodeList = doc.getElementsByTagName("item");

        for(int i =0; i<nodeList.getLength(); i++){

            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;

            NodeList itemId = fstElmnt.getElementsByTagName("itemId");
            s += "itemId= " + itemId.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList  title = fstElmnt.getElementsByTagName("title");
            s += "title= " + title.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList description = fstElmnt.getElementsByTagName("description");
            s += "description= " + description.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList coverSmallUrl = fstElmnt.getElementsByTagName("coverSmallUrl");
            s += "coverSmallUrl= " + coverSmallUrl.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList publisher = fstElmnt.getElementsByTagName("itemId");
            s += "itemId= " + publisher.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList author = fstElmnt.getElementsByTagName("author");
            s += "author= " + author.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList rank= fstElmnt.getElementsByTagName("rank");
            s += "rank= " + rank.item(0).getChildNodes().item(0).getNodeValue() + "\n";

            NodeList customerReviewRank = fstElmnt.getElementsByTagName("customerReviewRank");
            s += "customerReviewRank= " + customerReviewRank.item(0).getChildNodes().item(0).getNodeValue() + "\n";

//            NodeList isbn = fstElmnt.getElementsByTagName("isbn");
//            s += "isbn= " + isbn.item(0).getChildNodes().item(0).getNodeValue() + "\n";

        }


        textview.setText(s);

        super.onPostExecute(doc);

    }

}