package org.ln.noortools.view.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.ln.noortools.i18n.I18n;
import org.ln.noortools.service.RenamerService;
import org.ln.noortools.tag.AbstractTag;
import org.ln.noortools.view.TagListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.miginfocom.swing.MigLayout;



@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class TagPanel extends AbstractPanelContent {

	private final RenamerService renamerService;
	private JList<AbstractTag> tagList;
	private JScrollPane tagListScrollPane;
	private JLabel tagLabel;
	//private FillOptionsPanel fill;
	
	  private final TagListModel tagListModel; // 👈 injected by Spring
	    @Autowired
	public TagPanel(I18n i18n, RenamerService renamerService, TagListModel tagListModel) {
        super(i18n);
        this.renamerService = renamerService;
        this.tagListModel = tagListModel; // ✅ use injected model
	}


	    /** Costruisce l’interfaccia grafica dopo l’iniezione di tutti i bean */
	    
	    @PostConstruct
	    private void initUI() {
	        buildUI(this);
	    }
	
//	    @PostConstruct
//	    private void buildUI() {
//	        initComponents(this);
//	    }
		
	    private void buildUI(JPanel contentArea) {

	    	contentArea.setLayout(new MigLayout(
	                "fill, insets 15",      // padding interno
	                "[grow]",               // una colonna che cresce
	                "[][][grow]"            // etichetta, campo, lista
	        ));

	        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        // 🔹 Etichetta
	        tagLabel = new JLabel("Available Tags");
	        tagLabel.setFont(tagLabel.getFont().deriveFont(13f));

	        // 🔹 Campo di testo ereditato da AbstractPanelContent
	        renameField.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(new Color(200, 200, 200)),
	                new EmptyBorder(5, 5, 5, 5)
	        ));

	        // 🔹 Lista tag
	        tagList = new JList<>(tagListModel);
	        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        tagList.setVisibleRowCount(8);
	        tagList.setBorder(BorderFactory.createCompoundBorder(
	                BorderFactory.createLineBorder(new Color(220, 220, 220)),
	                new EmptyBorder(5, 5, 5, 5)
	        ));
	        tagList.setBackground(new Color(250, 250, 250));

	        // Scroll pane con bordo arrotondato
	        JScrollPane scrollPane = new JScrollPane(tagList);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
	        scrollPane.getViewport().setBackground(Color.WHITE);

	        // Doppio click → aggiunge il tag al campo
	        tagList.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                if (e.getClickCount() == 2) {
	                    int index = tagList.locationToIndex(e.getPoint());
	                    if (index >= 0) {
	                        AbstractTag tag = tagList.getModel().getElementAt(index);
	                        renameField.setText(renameField.getText() + tag.getTagString());
	                    }
	                }
	            }
	        });

	        // 🔹 Aggiunta dei componenti con MigLayout
	        contentArea.add(tagLabel, "wrap");
	        contentArea.add(renameField, "growx, h 28!, wrap");
	        contentArea.add(scrollPane, "grow, push, h 250!");
	    }
	    
	@Override
	protected void initComponents(JPanel contentArea) {
//		tagLabel = new JLabel("Nuovo nome");
//		tagListScrollPane = new JScrollPane();
//		tagList = new JList<AbstractTag>();
//		tagList.setModel(tagListModel);
//		tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		tagListScrollPane.setViewportView(tagList);
//		tagList.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					int index = tagList.locationToIndex(e.getPoint());
//					if (index >= 0) {
//						String values = tagList.getModel().getElementAt(
//								tagList.locationToIndex(e.getPoint())).getTagString();
//						renameField.setText(renameField.getText() + values);
//					}
//				}
//			}
//		});
//		
//		contentArea.setLayout(new MigLayout("", "[grow]", "[][][][]"));
//		contentArea.add(tagLabel, 			"wrap");
//		contentArea.add(renameField, 		"growx, wrap, w :150:");
//		contentArea.add(tagListScrollPane, 	"growx, growy, wrap,  h :250:"); 
		
	}

	
	protected void initComponents() {
		
		renameField.getDocument().addDocumentListener(this);
		
		
		



//		fill = new FillOptionsPanel();
//		fill.addChangeListener(this);
		setLayout(new MigLayout("", "[grow]", "[][][][]"));
		add(tagLabel, 			"wrap");
		add(renameField, 		"growx, wrap, w :150:");
		add(tagListScrollPane, 	"growx, growy, wrap,  h :250:"); 
		//add(fill 				); 
	}


	@Override
	protected
	void updateView() {
//		RnPrefs.getInstance().setGlobalProperty(
//				"FILL_TYPE", fill.getSelectedOption().name());
//		RnPrefs.getInstance().setGlobalProperty(
//				"FILL_VALUE", fill.getStringValue());
//		if(StringParser.isParsable(renameField.getText())) {
//			accordion.setTableData(StringParser.parse(
//					renameField.getText(), accordion.getTableData())) ;
//		}
		
	}





}
