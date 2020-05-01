package com.hku.cs_cinema.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.hku.cs_cinema.OnSeatClickListener;
import com.hku.cs_cinema.R;
import com.hku.cs_cinema.model.Seat;
import com.hku.cs_cinema.model.SeatInfo;

public class SSView extends View {
	Context mContext;
	int x_offset = 0;

	private Bitmap mBitMapSeatNormal = null;

	private Bitmap mBitMapSeatLock = null;

	private Bitmap mBitMapSeatChecked = null;


	private Canvas mCanvas = null;


	private boolean U = false;


	private int ss_seat_current_height = 57;

	private int ss_seat_current_width = 57;

	private int L = 5;
	private double T = 1.0D;

	private double t = -1.0D;
	private double u = 1.0D;

	private boolean v = false;


	private int ss_seat_min_height = 0;

	private int ss_seat_max_height = 0;

	private int ss_seat_min_width = 0;

	private int ss_seat_max_width = 0;

	private OnSeatClickListener mOnSeatClickListener = null;

	public static double a = 1.0E-006D;
	private int I = 0;
	private int ss_between_offset = 2;
	private int ss_seat_check_size = 50;
	private SSThumView mSSThumView = null;
	private int ss_seat_thum_size_w = 120;
	private int ss_seat_thum_size_h = 90;
	private int ss_seat_rect_line = 2;
	private Bitmap mBitMapThumView = null;
	private volatile int V = 1500;
	private int h = 0;
	private int i = 0;
	private int j = 0;
	private int k = 0;
	private float n = 0.0F;
	private float o = 0.0F;
	private int p = 0;
	private int q = 0;
	private int r = 0;
	private int s = 0;
	private boolean w = true;
	private boolean first_load_bg = true;
	private int tempX;
	private int tempY;
	
	GestureDetector mGestureDetector = new GestureDetector(mContext,
			new GestureListener(this));

	private ArrayList<SeatInfo> mListSeatInfos = null;
	private ArrayList<ArrayList<Integer>> mListSeatConditions = null;
	private int iMaxPay = 0;
	private int totalCountEachRow;
	private int rows;

	public SSView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public SSView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.mContext = paramContext;
	}

	public void init(int row_count, int rows,
			ArrayList<SeatInfo> list_seatInfos,
			ArrayList<ArrayList<Integer>> list_seat_condtions,
			SSThumView paramSSThumView, int imaxPay) {
		this.iMaxPay = imaxPay;
		this.mSSThumView = paramSSThumView;
		this.totalCountEachRow = row_count;
		this.rows = rows;
		this.mListSeatInfos = list_seatInfos;
		this.mListSeatConditions = list_seat_condtions;
		this.mBitMapSeatNormal = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.seat_normal));
		this.mBitMapSeatLock = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.seat_lock));
		this.mBitMapSeatChecked = getBitmapFromDrawable((BitmapDrawable) this.mContext
				.getResources().getDrawable(R.drawable.seat_checked));

		this.ss_seat_thum_size_w = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_w);
		this.ss_seat_thum_size_h = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_h);
		
		this.ss_seat_max_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_max_height);
		this.ss_seat_max_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_max_width);
		this.ss_seat_min_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_min_height);
		this.ss_seat_min_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_min_width);
		this.ss_seat_current_height = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_height);
		this.ss_seat_current_width = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_width);
		this.ss_seat_check_size = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_check_size);//30dp
		this.ss_between_offset = this.mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_between_offset);//5dp
		invalidate();
	}

	public static Bitmap getBitmapFromDrawable(
			BitmapDrawable paramBitmapDrawable) {
		return paramBitmapDrawable.getBitmap();
	}

	private void a(int seatNum, int rowNum, Bitmap paramBitmap,
			Canvas paramCanvas1, Canvas paramCanvas2, Paint paramPaint) {
		if (paramBitmap == null) {
			paramCanvas1.drawRect(c(seatNum, rowNum), paramPaint);
			if (this.U) {
				paramCanvas2.drawRect(d(seatNum, rowNum), paramPaint);
			}
		} else {
			paramCanvas1.drawBitmap(paramBitmap, null, c(seatNum, rowNum),
					paramPaint);
			if (this.U) {
				paramCanvas2.drawBitmap(paramBitmap, null, d(seatNum, rowNum),
						paramPaint);
			}
		}
	}

	private Rect c(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(this.h + seatNum * this.ss_seat_current_width + this.L,
					this.j + rowNum * this.ss_seat_current_height + this.L, this.h + (seatNum + 1)
							* this.ss_seat_current_width - this.L, this.j + (rowNum + 1) * this.ss_seat_current_height
							- this.L);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	private Rect d(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(
					5 + (int) (this.T * (this.h + seatNum * this.ss_seat_current_width + this.L)),
					5 + (int) (this.T * (this.j + rowNum * this.ss_seat_current_height + this.L)),
					5 + (int) (this.T * (this.h + (seatNum + 1) * this.ss_seat_current_width - this.L)),
					5 + (int) (this.T * (this.j + (rowNum + 1) * this.ss_seat_current_height - this.L)));
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	private Rect e(int paramInt1, int paramInt2) {
		int i1;
		int i3;
		try {
			if (getMeasuredWidth() < this.r) {
				i1 = getMeasuredWidth();
			} else {
				i1 = this.r;
			}
			if (getMeasuredHeight() < this.s) {
				i3 = getMeasuredHeight();
			} else {
				i3 = this.s;
			}
			return new Rect((int) (5.0D + this.T * paramInt1),
					(int) (5.0D + this.T * paramInt2), (int) (5.0D + this.T
							* paramInt1 + i1 * this.T), (int) (5.0D + this.T
							* paramInt2 + i3 * this.T));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Rect();
		}
	}

	@Override
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		if (this.totalCountEachRow == 0 || this.rows == 0) {
			return;
		}

		if (this.n + this.r < 0.0f || this.o + this.s < 0.0f) {
			this.n = 0.0f;
			this.o = 0.0f;
			this.p = 0;
			this.q = 0;
		}
		Paint localPaint2 = new Paint();
		if (this.ss_seat_current_width != 0 && this.ss_seat_current_height != 0) {

			this.mBitMapThumView = Bitmap.createBitmap(this.ss_seat_thum_size_w,
					this.ss_seat_thum_size_h, Bitmap.Config.ARGB_8888);
			this.mCanvas = new Canvas();
			this.mCanvas.setBitmap(this.mBitMapThumView);
			this.mCanvas.save();
			
			 Paint localPaint1 = new Paint();
			 localPaint1.setXfermode(new PorterDuffXfermode(
			 PorterDuff.Mode.CLEAR));
			 this.mCanvas.drawPaint(localPaint1);
			 
			 double d1 = (this.ss_seat_thum_size_w - 10.0D)
						/ (this.ss_seat_current_width * this.totalCountEachRow + this.h + this.i);

			double d2 = (this.ss_seat_thum_size_h - 10.0D)
						/ (this.ss_seat_current_height * this.rows);
				if (d1 <= d2) {
					this.T = d1;
				} else {
					this.T = d2;
				}
			if(this.U){

				localPaint2.setColor(Color.BLACK);
				if(first_load_bg){
					first_load_bg = false;
					tempX = 5+(int) (this.r * this.T);
					tempY = 5 + (int) (this.s * this.T);
				}
				this.mCanvas.drawRect(5.0F, 5.0F,  tempX,
						tempY, localPaint2);
			}
		}

			paramCanvas.translate(this.n, this.o);
			this.r = this.h + this.ss_seat_current_width * this.totalCountEachRow + this.i;
			this.s = this.ss_seat_current_height * this.rows;

			this.h = (int) Math.round(this.ss_seat_current_width / 2.0D);
			localPaint2.setTextAlign(Paint.Align.CENTER);
			localPaint2.setAntiAlias(true);
			localPaint2.setColor(Color.BLACK);
			for (int i2 = 0; i2 < this.mListSeatConditions.size(); i2++) {
				ArrayList<Integer> localArrayList = this.mListSeatConditions
						.get(i2);

				for (int i3 = 0; i3 < this.mListSeatInfos.get(i2).getSeatList()
						.size(); i3++) {// 2344
					// goto5 - 2344
					Seat localSeat = this.mListSeatInfos.get(i2).getSeat(i3);
					switch (localArrayList.get(i3).intValue()) { // 2373
					case 0:
						localPaint2.setColor(0);
						a(i3, i2, null, paramCanvas, this.mCanvas, localPaint2);
						localPaint2.setColor(Color.BLACK);
						break;
					case 1:
						a(i3, i2, this.mBitMapSeatNormal, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					case 2:
						a(i3, i2, this.mBitMapSeatLock, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					case 3:
						a(i3, i2, this.mBitMapSeatChecked, paramCanvas,
								this.mCanvas, localPaint2);
						break;
					default:
						break;
					}
				}
				// cond_d - 2538
			}

			localPaint2.setTextSize(0.4F * this.ss_seat_current_height);
			for (int i1 = 0; i1 < this.mListSeatInfos.size(); i1++) {
				localPaint2.setColor(0xffffffff-1308622847);
				paramCanvas.drawRect(new Rect((int) Math.abs(this.n), this.j
						+ i1 * this.ss_seat_current_height, (int) Math.abs(this.n) + this.ss_seat_current_width / 2,
						this.j + (i1 + 1) * this.ss_seat_current_height), localPaint2);
				localPaint2.setColor(-1);
				paramCanvas
						.drawText(this.mListSeatInfos.get(i1)
								.getDesc(), (int) Math.abs(this.n) + this.ss_seat_current_width / 2
								/ 2, this.j + i1 * this.ss_seat_current_height + this.ss_seat_current_height / 2 + this.k
								/ 2, localPaint2);
			}


		if (this.U) {
			localPaint2.setColor(0xffffffff-739327);
			localPaint2.setStyle(Paint.Style.STROKE);
			localPaint2.setStrokeWidth(this.ss_seat_rect_line);
			this.mCanvas.drawRect(
					e((int) Math.abs(this.n), (int) Math.abs(this.o)),
					localPaint2);
			localPaint2.setStyle(Paint.Style.FILL);
			// paramCanvas.restore();
			this.mCanvas.restore();
		}

		if (this.mSSThumView != null) {
			this.mSSThumView.a(mBitMapThumView);
			this.mSSThumView.invalidate();
		}

}

	public void setXOffset(int x_offset) {
		this.x_offset = x_offset;
	}

	private float getTwoPoiniterDistance(MotionEvent paramMotionEvent) {
		float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
		float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
		return (float)Math.sqrt(f1 * f1 + f2 * f2);
	}

	private void a(MotionEvent paramMotionEvent) {
		double d1 = getTwoPoiniterDistance(paramMotionEvent);
		if (this.t < 0.0D) {
			this.t = d1;
		} else {
			try {
				this.u = (d1 / this.t);
				this.t = d1;
				if ((this.v) && (Math.round(this.u * this.ss_seat_current_width) > 0L)
						&& (Math.round(this.u * this.ss_seat_current_height) > 0L)) {
					this.ss_seat_current_width = (int) Math.round(this.u * this.ss_seat_current_width);
					this.ss_seat_current_height = (int) Math.round(this.u * this.ss_seat_current_height);
					this.h = (int) Math.round(this.ss_seat_current_width / 2.0D);
					this.i = this.h;
					this.L = (int) Math.round(this.u * this.L);
					if (this.L <= 0)
						this.L = 1;
				}
				invalidate();
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}

	}


	public static int m(SSView mSsView) {
		return mSsView.h;
	}

	public static int m(SSView mSsView, int paramInt) {
		mSsView.V = mSsView.V - paramInt;
		return mSsView.V;
	}

	public static int x(SSView mSsView) {
		return mSsView.V;
	}

	public static void y(SSView mSsView) {
		mSsView.a();
	}

	private void a() {

	}


	public static float w(SSView mSsView) {
		return mSsView.o;
	}

	public static float v(SSView mSsView) {
		return mSsView.n;
	}

	public static int u(SSView mSsView) {
		return mSsView.s;
	}

	public static int t(SSView mSsView) {
		return mSsView.q;
	}

	public static int s(SSView mSsView) {
		return mSsView.r;
	}

	public static int r(SSView mSsView) {
		return mSsView.p;
	}

	public static int q(SSView mSsView) {
		return mSsView.j;
	}

	public static int p(SSView mSsView) {
		return mSsView.rows;
	}

	public static int o(SSView mSsView) {
		return mSsView.i;
	}

	public static int n(SSView mSsView) {
		return mSsView.totalCountEachRow;
	}

	public static int l(SSView mSsView, int paramInt) {
		mSsView.q = mSsView.q + paramInt;
		return mSsView.q;
	}


	public static int l(SSView mSsView) {
		return mSsView.ss_seat_current_width;
	}


	public static int k(SSView mSsView) {
		return mSsView.ss_seat_current_width;
	}

	public static int k(SSView mSsView, int paramInt) {
		mSsView.p = mSsView.p + paramInt;
		return mSsView.p;
	}

	public static int j(SSView mSsView) {
		return mSsView.ss_seat_current_height;
	}


	public static int j(SSView mSsView, int paramInt) {
		mSsView.q = paramInt;
		return mSsView.q;
	}

	public static int i(SSView mSsView, int paramInt) {
		mSsView.p = paramInt;
		return mSsView.p;
	}


	public static boolean i(SSView mSsView) {
		return mSsView.U;
	}


	public static int h(SSView mSsView, int paramInt) {
		return mSsView.s;
	}

	public static int h(SSView mSsView) {
		return mSsView.I + 1;
	}


	public static int g(SSView mSsView, int paramInt) {
		return mSsView.r;
	}

	public static int getImaxPay(SSView mSsView) {
		return mSsView.iMaxPay;
	}


	public static boolean a(SSView mSsView, boolean param) {
		mSsView.U = param;
		return mSsView.U;
	}

	public static float a(SSView mSsView, float param) {
		mSsView.n = param;
		return mSsView.n;
	}

	public static int a(SSView mSsView, int param) {
		return mSsView.a(param);
	}

	private int a(int paramInt) {
		try {
			int i1 = (paramInt + this.p - this.h) / this.ss_seat_current_width;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	public static Rect a(SSView mSsView, int param1, int param2) {
		return mSsView.f(param1, param2);
	}

	private Rect f(int paramInt1, int paramInt2) {
		try {
			int v1 = this.ss_seat_current_width * paramInt1 + this.h - this.p - this.L;
			int v2 = this.ss_seat_current_height * paramInt2 + this.j - this.q - this.L;
			int v3 = (paramInt1 + 1) * this.ss_seat_current_width + this.h - this.p + this.L;
			int v4 = (this.j + 1) * this.ss_seat_current_height + this.j - this.q + this.L;
			return new Rect(v1, v2, v3, v4);
		} catch (Exception e) {
			e.printStackTrace();
			return new Rect();
		}
	}


	public static boolean a(SSView mSsView) {
		return mSsView.w;
	}
	
	private int b() {
		return Math.round(this.ss_seat_current_width / this.ss_seat_check_size
				* this.ss_between_offset);
	}


	public static float c(SSView mSsView, float param) {
		mSsView.n = mSsView.n - param;
		return mSsView.n;
	}


	public static float c(SSView mSsView, int param) {
		mSsView.ss_seat_current_height = param;
		return mSsView.ss_seat_current_height;
	}


	public static ArrayList c(SSView mSsView) {
		return mSsView.mListSeatInfos;
	}


	public static float d(SSView mSsView, float param) {
		mSsView.o = mSsView.o - param;
		return mSsView.o;
	}


	public static int d(SSView mSsView, int param) {
		mSsView.ss_seat_current_width = param;
		return mSsView.ss_seat_current_width;
	}


	public static OnSeatClickListener d(SSView mSsView) {
		return mSsView.mOnSeatClickListener;
	}


	public static float b(SSView mSsView, float param) {
		mSsView.o = param;
		return mSsView.o;
	}


	public static int b(SSView mSsView, int param) {
		return mSsView.b(param);
	}


	public static ArrayList b(SSView mSsView) {
		return mSsView.mListSeatConditions;
	}


	private int b(int paramInt) {
		try {
			int i1 = (paramInt + this.q - this.j) / this.ss_seat_current_height;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}


	public static int e(SSView mSsView, int param) {
		mSsView.h = param;
		return mSsView.h;
	}


	public static int e(SSView mSsView) {
		mSsView.I--;
		return mSsView.I;
	}


	public static int f(SSView mSsView) {
		return mSsView.I;
	}


	public static int f(SSView mSsView, int param) {
		mSsView.i = param;
		return mSsView.i;
	}


	public void setOnSeatClickListener(
			OnSeatClickListener paramOnSeatClickLinstener) {
		this.mOnSeatClickListener = paramOnSeatClickLinstener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getPointerCount() == 1) {
			if (this.v) {
				this.v = false;
				this.w = false;
				this.t = -1.0D;
				this.u = 1.0D;
			}else{
				this.w = true;
			}

			while (this.ss_seat_current_width < this.ss_seat_min_width || this.ss_seat_current_height < this.ss_seat_min_height) {
				this.ss_seat_current_width++;
				this.ss_seat_current_height++;
				this.h = (int) Math.round(this.ss_seat_current_width / 2.0D);
				this.i = this.h;
				 this.L = b();
				SSView.i(this, 0);
				SSView.a(this, 0.0F);
				SSView.j(this, 0);
				SSView.b(this, 0.0F);
				invalidate();
			}
			while ((this.ss_seat_current_width > this.ss_seat_max_width) || (this.ss_seat_current_height > this.ss_seat_max_height)) {
				this.ss_seat_current_width--;
				this.ss_seat_current_height--;
				this.h = (int) Math.round(this.ss_seat_current_width / 2.0D);
				this.i = this.h;
				 this.L = b();
				invalidate();
			}

			this.mGestureDetector.onTouchEvent(event);
		} else {
			this.v = true;
			a(event);

		}

		return true;
	}

}
