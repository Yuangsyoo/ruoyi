package com.ruoyi.common.sdk;
import com.sun.jna.Pointer;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;


public class LPRDemo {

	        static LPRSDK lpr= LPRSDK.INSTANCE;
	         VZLPRC_PLATE_INFO_CALLBACK PlateCallBack = new VZLPRC_PLATE_INFO_CALLBACK();
	         VZDEV_SERIAL_RECV_DATA_CALLBACK SerialCallBack = new VZDEV_SERIAL_RECV_DATA_CALLBACK();
	         VZLPRC_FIND_DEVICE_CALLBACK_EX findCallBack = new VZLPRC_FIND_DEVICE_CALLBACK_EX();
	         VZLPRC_WLIST_QUERY_CALLBACK wlistCallback = new VZLPRC_WLIST_QUERY_CALLBACK();

	//关闭设备得控制句柄
	public void VzLPRClient_Close(int handle){
	lpr.VzLPRClient_Close(handle);

	}

	//释放资源
	public void VzLPRClient_Cleanup(){
	   lpr.VzLPRClient_Cleanup();
	}

	//开杆
	public int switchOn(int handle, int uChnId, int nDuration){
	 int i = lpr.VzLPRClient_SetIOOutputAuto(handle,uChnId,nDuration);
	 return i;
	}

	public   int InitClient(String ip){

			lpr.VzLPRClient_Setup();
		   //连接设备返回句柄
			int handle = lpr.VzLPRClient_Open(ip, 80, "admin", "admin");
			return handle;

	}

	// 测试485数据发送
	public void SendSerialData(int handle, List<byte[]>list) {

		// 485测试(串口句柄只用打开一次)
		int serial_handle = lpr.VzLPRClient_SerialStart(handle, 0, SerialCallBack, Pointer.NULL);
		for (byte[] bytes : list) {
			try {
				// 可以发任意多次的485数据
				int ret = lpr.VzLPRClient_SerialSend(serial_handle, bytes, bytes.length);
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 使用完，关闭串口
		lpr.VzLPRClient_SerialStop(serial_handle);
	}


	// 根据id获取车牌记录图片数据
	public boolean LoadImg(int handle, int id) {
		boolean ret = false;

		int max_size = 1920 * 1080;
		int[] size = new int[1];

		size[0] = max_size;
		byte[] img_data = new byte[max_size];

		int ret_load = lpr.VzLPRClient_LoadImageById(handle, id, img_data, size);
		if (ret_load == 0){
			ret = true;
		}

		return ret;
	}
	// 添加白名单
	public int AddWlistPlate( int handle, String plate) {
		int ret = 0;

		try
		{
			LPRSDK.VZ_LPR_WLIST_VEHICLE.ByReference wlistVehicle = new LPRSDK.VZ_LPR_WLIST_VEHICLE.ByReference();

			byte[] plate_data = plate.getBytes("GB2312");
			for (int i = 0; i < plate_data.length; i++) {
				wlistVehicle.strPlateID[i] = plate_data[i];
				wlistVehicle.strCode[i] = plate_data[i];
			}

			wlistVehicle.uCustomerID	= 0;
			wlistVehicle.bEnable		= 1;

			LPRSDK.VZ_TM.ByValue struTMOverdule = new LPRSDK.VZ_TM.ByValue();
			struTMOverdule.nYear	= 2020;
			struTMOverdule.nMonth	= 12;
			struTMOverdule.nMDay	= 30;
			struTMOverdule.nHour	= 12;
			struTMOverdule.nMin		= 40;
			struTMOverdule.nSec		= 50;

			wlistVehicle.struTMOverdule	= struTMOverdule;
			wlistVehicle.bUsingTimeSeg	= 0;
			wlistVehicle.bAlarm			= 0;
			wlistVehicle.bEnableTMOverdule = 1;

			LPRSDK.VZ_LPR_WLIST_ROW.ByReference wlistRow	= new LPRSDK.VZ_LPR_WLIST_ROW.ByReference();
			wlistRow.pVehicle = wlistVehicle;
			wlistRow.pCustomer = null;
			LPRSDK.VZ_LPR_WLIST_IMPORT_RESULT.ByReference importResult = new LPRSDK.VZ_LPR_WLIST_IMPORT_RESULT.ByReference();
			ret = lpr.VzLPRClient_WhiteListImportRows(handle, 1, wlistRow, importResult);
		}
		catch(Exception e)
		{
			System.out.println("error:" + e.getMessage());
		}

		return ret;
	}

	// 根据车牌号删除白名单
	public boolean DeleteWlistByPlate(int handle, String plate) {
		boolean ret = false;
		try {
			byte[] plate_data = plate.getBytes("GB2312");

			byte[] value2 = new byte[plate_data.length + 1];
			for (int i = 0; i < plate_data.length; i++) {
				value2[i] = plate_data[i];
			}
			int ret_delete = lpr.VzLPRClient_WhiteListDeleteVehicle(handle, value2);
			if (ret_delete == 0) {
				ret = true;
			}
		}
		catch(UnsupportedEncodingException e)
		{
			System.out.println("exception msg:" + e.getMessage());
		}

		return ret;
	}

	public boolean QueryWlistByPlate(int handle, String plate) {
		boolean ret = false;
		try {
			byte[] plate_data = plate.getBytes("GB2312");

			byte[] value2 = new byte[plate_data.length + 1];
			for (int i = 0; i < plate_data.length; i++) {
				value2[i] = plate_data[i];
			}
			int res = lpr.VzLPRClient_WhiteListQueryVehicleByPlate(handle,value2);
			if (res == 0) {
				ret = true;
			}
		}
		catch(UnsupportedEncodingException e)
		{
			System.out.println("exception msg:" + e.getMessage());
		}

		return ret;
	}

	public boolean QueryWlistByPage(int handle, int nPageIndex) {
		boolean ret = false;

		// 设置起始范围
		LPRSDK.VZ_LPR_WLIST_LIMIT.ByReference limit = new LPRSDK.VZ_LPR_WLIST_LIMIT.ByReference();
		limit.limitType = 2; // LPRSDK.VZ_LPR_WLIST_LIMIT_TYPE.VZ_LPR_WLIST_LIMIT_TYPE_RANGE;

		LPRSDK.VZ_LPR_WLIST_RANGE_LIMIT.ByReference rangLimit = new LPRSDK.VZ_LPR_WLIST_RANGE_LIMIT.ByReference();
		rangLimit.startIndex = nPageIndex * 10;
		rangLimit.count = 10;
		limit.pRangeLimit = rangLimit;

		// 查询条件
		LPRSDK.VZ_LPR_WLIST_SEARCH_CONSTRAINT.ByReference searchConstraint = new LPRSDK.VZ_LPR_WLIST_SEARCH_CONSTRAINT.ByReference();
		// strcpy(searchConstraint.key, "PlateID");
		// strcpy_s(searchConstraint.search_string, sizeof(searchConstraint.search_string), m_sPlate.c_str());
		searchConstraint.key[0]= 'P';
		searchConstraint.key[1]= 'l';
		searchConstraint.key[2]= 'a';
		searchConstraint.key[3]= 't';
		searchConstraint.key[4]= 'e';
		searchConstraint.key[5]= 'I';
		searchConstraint.key[6]= 'D';

		LPRSDK.VZ_LPR_WLIST_SEARCH_WHERE.ByReference searchWhere = new LPRSDK.VZ_LPR_WLIST_SEARCH_WHERE.ByReference();
		searchWhere.pSearchConstraints = searchConstraint;
		searchWhere.searchConstraintCount = 1;
		searchWhere.searchType = 0; // LPRSDK.VZ_LPR_WLIST_SEARCH_TYPE.VZ_LPR_WLIST_SEARCH_TYPE_LIKE;

		LPRSDK.VZ_LPR_WLIST_LOAD_CONDITIONS.ByReference conditions = new LPRSDK.VZ_LPR_WLIST_LOAD_CONDITIONS.ByReference();
		conditions.pSearchWhere = searchWhere;
		conditions.pLimit = limit;

		int[] count = new int[1];
		lpr.VzLPRClient_WhiteListGetVehicleCount(handle, count, searchWhere);
		System.out.println("count:" + count[0]);

		int res = lpr.VzLPRClient_WhiteListLoadVehicle(handle, conditions);
		if (res == 0) {
			ret = true;
		}

		return ret;
	}

	// 线圈参数配置
	public void TestLoopParam(int handle) {
		LPRSDK.VZ_LPRC_VIRTUAL_LOOPS_EX.ByReference pLoopInfo = new LPRSDK.VZ_LPRC_VIRTUAL_LOOPS_EX.ByReference();
		lpr.VzLPRClient_GetVirtualLoopEx(handle, pLoopInfo);
		pLoopInfo.struLoop[0].eCrossDir = 1;
		lpr.VzLPRClient_SetVirtualLoopEx(handle, pLoopInfo);
	}

	// 设置相机时间
	public int SetDateTime(int handle) {
		LPRSDK.VZ_DATE_TIME_INFO.ByReference pDTInfo = new LPRSDK.VZ_DATE_TIME_INFO.ByReference();
		pDTInfo.uYear = 2019;
		pDTInfo.nMonth = 5;
		pDTInfo.nMDay = 6;
		pDTInfo.nHour = 12;
		pDTInfo.nMin  = 30;
		pDTInfo.nSec  = 40;

		int ret = lpr.VzLPRClient_SetDateTime(handle, pDTInfo);
		return ret;
	}

	// 设置osd参数
	public int SetOSDParam(int handle) {
		LPRSDK.VZ_LPRC_OSD_Param.ByReference osd_param = new LPRSDK.VZ_LPRC_OSD_Param.ByReference();
		lpr.VzLPRClient_GetOsdParam(handle, osd_param);

		osd_param.overlaytext[0] = '1';
		osd_param.overlaytext[1] = '2';
		osd_param.overlaytext[2] = '3';
		osd_param.overlaytext[3] = '4';
		osd_param.overlaytext[4] = 0;
		int ret = lpr.VzLPRClient_SetOsdParam(handle, osd_param);
		return ret;
	}

	// 获取在线状态
	public int GetIsConnect(int handle) {
		byte[] state = new byte[1];
		int ret = lpr.VzLPRClient_IsConnected(handle, state);
		System.out.println("state:" + state[0]);

		return ret;
	}




	// 测试截图
	public int SnapImg(int handle) {
		int img_len = 1280 * 720;
		byte[] img_data = new byte[img_len];
		int real_len = lpr.VzLPRClient_GetSnapImage(handle, img_data, img_len);

		System.out.println("img data len:" + real_len);

		return real_len;
	}

	//通过该回调函数获得车牌识别信息图片
	public class VZLPRC_PLATE_INFO_CALLBACK implements LPRSDK.VZLPRC_PLATE_INFO_CALLBACK {
		public void invoke(int handle,Pointer pUserData,LPRSDK.TH_PlateResult_Pointer.ByReference pResult,int uNumPlates,
				int eResultType,LPRSDK.VZ_LPRC_IMAGE_INFO_Pointer.ByReference pImgFull,LPRSDK.VZ_LPRC_IMAGE_INFO_Pointer.ByReference pImgPlateClip)
		{
			int type = LPRSDK.VZ_LPRC_RESULT_TYPE.VZ_LPRC_RESULT_REALTIME.ordinal();
			if(eResultType != type)
			{
				// 根据设备句柄，获取当前回调结果设备的IP
				byte[] device_ip = new byte[32];
				lpr.VzLPRClient_GetDeviceIP(handle, device_ip, 32);

				String ip = new String(device_ip);
				System.out.println("device ip:" + ip);

				try {
					String license = new String(pResult.license, "gb2312");
					System.out.println(license);

					System.out.println("nIsFakePlate:" + pResult.nIsFakePlate);

					String path = "./" + pResult.struBDTime.bdt_year + pResult.struBDTime.bdt_mon
							+ pResult.struBDTime.bdt_mday + pResult.struBDTime.bdt_hour
							+ pResult.struBDTime.bdt_min + pResult.struBDTime.bdt_sec + ".jpg";
					lpr.VzLPRClient_ImageSaveToJpeg(pImgFull, path, 100);
				}
				catch(UnsupportedEncodingException e)
				{
					System.out.println("exception msg:" + e.getMessage());
				}
			}
		}
	}

	// 通过该回调函数获得透明通道接收的数据
	public class VZDEV_SERIAL_RECV_DATA_CALLBACK implements LPRSDK.VZDEV_SERIAL_RECV_DATA_CALLBACK {
		public void invoke(int handle,Pointer pRecvData , int uRecvSize, Pointer pUserData )
		{
			byte[] serial_data = pRecvData.getByteArray(0, uRecvSize);

			System.out.println("data size:" + uRecvSize);
		}
	}

	public class VZLPRC_FIND_DEVICE_CALLBACK_EX implements LPRSDK.VZLPRC_FIND_DEVICE_CALLBACK_EX {
		public void invoke(String pStrDevName, String pStrIPAddr, short usPort1, short usType, long SL, long SH, String netmask, String gateway, Pointer pUserData)
		{
			System.out.println("find ip:" + pStrIPAddr);
		}
	}

	//白名单表和客户信息表的查询反馈结果函数
	public class VZLPRC_WLIST_QUERY_CALLBACK implements LPRSDK.VZLPRC_WLIST_QUERY_CALLBACK {
		public void invoke( int cbtype,LPRSDK.VZ_LPR_WLIST_VEHICLE.ByReference vehicle,LPRSDK.VZ_LPR_WLIST_CUSTOMER.ByReference pCustomer,Pointer UserData)
		{
			System.out.println("VZLPRC_WLIST_QUERY_CALLBACK");

			try {
				String plate = new String(vehicle.strPlateID, "gb2312");
				System.out.println(plate);
			}
			catch(UnsupportedEncodingException e)
			{
				System.out.println("exception msg:" + e.getMessage());
			}
		}
	}



}
