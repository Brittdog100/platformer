package asset.render;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class UILabel extends JLabel{
	private static final long serialVersionUID = -4245765030404599408L;
	private long id;

	public UILabel(){super();}
	public UILabel(ImageIcon arg){super(arg);}

	public void setIdentity(long arg){id = arg;}
	public long getIdentity(){return id;}

}
