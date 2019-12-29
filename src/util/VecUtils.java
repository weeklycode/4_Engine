package util;

public class VecUtils {

	public static double scalarProject(double[] vec, double[] point){
		double x;
		if ((x=vec[0]*vec[0]+vec[1]*vec[1]) == 0)
			return 0;
		x=Math.sqrt(x);
		System.out.printf("div0=%f,div1=%f\n",divideScalar(point,x)[0],divideScalar(point,x)[1]);
		return dot(point,norm(vec));
	}

	public static double lengthOf(double[] vec){
		return Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]);
	}

	public static double[] multiplyScalar(double scale, double[] vec){
		return new double[]{
				vec[0]*scale,
				vec[1]*scale
		};
	}

	public static double[] divideScalar(double[] num, double denom){
		return new double[]{
				num[0]/denom,
				num[1]/denom
		};
	}

	public static double dot(double[] vec1, double[] vec2){
		return vec1[0]*vec2[0]+vec1[1]*vec2[1];
	}

	public static double[] subtract(double[] pos, double[] minus){
		return new double[]{
				pos[0]-minus[0],
				pos[1]-minus[1]
		};
	}

	public static double[] norm(double[] vec){
		return divideScalar(vec,lengthOf(vec));
	}
}
