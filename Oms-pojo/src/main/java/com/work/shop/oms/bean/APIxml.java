package com.work.shop.oms.bean;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


public class APIxml {
	private Element root;
	public APIxml(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		this.root = reader.read(file).getRootElement();
	}

	public APIxml(String xml) throws DocumentException {
		try {
			SAXReader reader = new SAXReader();
			StringReader sr = new StringReader(xml);
			this.root = reader.read(sr).getRootElement();
		} catch (Exception e) {
			this.root = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ShippingOrg toObject(){
		ShippingOrg org = new ShippingOrg();
		List<TrPriceInfo> pinfos = new ArrayList<TrPriceInfo>();
		if(root == null){
			return org;
		}
		try {
			Node isOkNode = root.element("ISOK");
			Node msgNode = root.element("MESSAGE");
			org.setMsg(msgNode.getText());
			
			org.setOrgOk(new Boolean(isOkNode.getText()));
		} catch (Exception e) {
			org.setOrgOk(false);
		}
		Node orgItemNode = null;
		List<Node> trPriceInfos = null;
		try {
			orgItemNode = root.element("OrgItem");
			trPriceInfos = ((Element)orgItemNode).elements("TrPriceInfo");
			for(Node node : trPriceInfos){
				Element ele = (Element)node;
				TrPriceInfo info = new TrPriceInfo();
				info.setDefault(Boolean.valueOf(ele.elementText("IS_DEFAULT")));
				info.setEnterPrice(Double.parseDouble(ele.elementText("ENTER_PRICE")));
				info.setEntityState(ele.elementText("EntityState"));
				info.setId(Integer.parseInt(ele.elementText("ID")));
				info.setOrgTransCode(ele.elementText("ORG_TRANS_CODE"));
				info.setOrgTransId(Integer.parseInt(ele.elementText("ORG_TRANS_ID")));
				info.setOrgTransName(ele.elementText("ORG_TRANS_NAME"));
				info.setSelected(Boolean.valueOf(ele.elementText("Selected")));
				info.setTransCycle(Integer.parseInt(ele.elementText("TRANS_CYCLE")));
				info.setTrTransLineId(Integer.parseInt(ele.elementText("TR_TRANS_LINE_ID")));
				pinfos.add(info);
			}
		} catch (Exception e) {
			return org;
		}
		org.setPriceInfos(pinfos);
		return org;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}
}
