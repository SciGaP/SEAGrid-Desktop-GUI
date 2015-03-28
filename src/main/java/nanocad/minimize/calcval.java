package nanocad.minimize;

public class calcval{
	public double theforce, thepotential, thestepsize;
	public calcval(double f, double p){
		this.theforce = f;
		this.thepotential = p;
		this.thestepsize = 0.0;
	}
	public calcval(double f, double p, double s){
		this.theforce = f;
		this.thepotential = p;
		this.thestepsize = s;
	}
}
