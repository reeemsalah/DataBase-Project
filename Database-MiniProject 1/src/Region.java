import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Shape;

public class Region extends Polygon implements Comparable {
	public Region(int[] xpoints, int[] ypoints, int npoints)
	{
		this.xpoints=xpoints;
		this.ypoints=ypoints;
		this.npoints=npoints;
	}
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Dimension dim1 = this.getBounds().getSize();
		Dimension dim2 = (Dimension) ((Region) o).getBounds().getSize();
		int area1 = dim1.height * dim1.width;
		int area2 = dim2.height * dim2.width;
		return area2 - area1;

	}
	@Override
	public String toString()
	{
		String res="";
		res+=this.npoints+",";
		for(int i=0;i<this.xpoints.length;i++)
			res+=this.xpoints[i]+",";
		for(int i=0;i<this.ypoints.length;i++) {
			if(i==this.ypoints.length-1)
				res+=this.ypoints[i];
			else
				res+=this.ypoints[i]+",";
		}
		return res;
	}
	public static void main(String[] args) {
		Region r1=new Region(new int[]{1,2,3},new int[]{4,5,6},3);
		Region r2=new Region(new int[]{0,0,0},new int[]{0,1,-1},3);
		System.out.println(r1);
		System.out.println(r2);
		System.out.println(r2.compareTo(r1));
	}
}