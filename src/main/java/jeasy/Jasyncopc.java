package jeasy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JEasyOpcExample;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.asynch.AsynchEvent;
import javafish.clients.opc.asynch.OpcAsynchGroupListener;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.CoInitializeException;
import javafish.clients.opc.exception.CoUninitializeException;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.GroupActivityException;
import javafish.clients.opc.exception.GroupUpdateTimeException;
import javafish.clients.opc.exception.ItemActivityException;
import javafish.clients.opc.variant.Variant;

public class Jasyncopc implements OpcAsynchGroupListener {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public static void main(String[] args) throws InterruptedException {
		Jasyncopc jasyncopc = new Jasyncopc();
		try {
			JOpc.coInitialize();
		} catch (CoInitializeException e1) {
			e1.printStackTrace();
		}

		JEasyOpc jopc = new JEasyOpc("localhost", "Matrikon.OPC.Simulation.1", "JOPC1");

		
		OpcItem item1 = new OpcItem("T100.LI101", true, "");
		OpcItem item2 = new OpcItem("T100.LI102", true, "");
		OpcItem item3 = new OpcItem("T100.LI103", true, "");
		OpcItem item4 = new OpcItem("T100.LI104", true, "");

		OpcGroup group = new OpcGroup("group1", true, 10000, 0.0f);

		group.addItem(item1);
		group.addItem(item2);
		group.addItem(item3);
		group.addItem(item4);
		
	
		jopc.addGroup(group);

		group.addAsynchListener(jasyncopc);

		jopc.start();

		synchronized (jasyncopc) {
			jasyncopc.wait(3000);
		}

		System.out.println("JOPC active: " + jopc.ping());

		synchronized (jasyncopc) {
			jasyncopc.wait(60000);
		}

	
	
		jopc.terminate();

		synchronized (jasyncopc) {
			jasyncopc.wait(300000);
		}

		try {
			JOpc.coUninitialize();
		} catch (CoUninitializeException e) {
			e.printStackTrace();
		}

	}

	public void getAsynchEvent(AsynchEvent event) {
		ArrayList<OpcItem> items = event.getOPCGroup().getItems();
		for (OpcItem item : items) {
			String itemName = item.getItemName();
			int itemType = item.getDataType();
			Variant itemValue = item.getValue();
			String itemTime = sdf.format(item.getTimeStamp().getTime());
			System.out.println(itemTime+"  "+itemName+"  "+itemType+"  "+itemValue);
			
		}


	}

}
