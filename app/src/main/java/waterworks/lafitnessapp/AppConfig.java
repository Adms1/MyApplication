package waterworks.lafitnessapp;

public class AppConfig {
	public static String NAMESPACE = "http://tempuri.org/";

	/**
	 * Client Local & Live
	 * Make build in Client's local app will automatically detect the server if 
	 * it didn't get the response then it'll change URLs to Live.
	 */
	//	public static String SOAP_ADDRESS = "http://192.168.1.201/WWWebService/Service.asmx?WSDL";
	//	public static String Report_Url = "http://192.168.1.201/newcode/";
	//Live
	public static String SOAP_ADDRESS_1 = "http://office.waterworksswimonline.com/WWWebService/Service.asmx/";
	public static String SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
	public static String Report_Url = "http://office.waterworksswimonline.com/newcode/";

//	Kishansir pc
//	public static String Report_Url = "http://office.waterworksswimonline.com/newcode/";
//	public static String SOAP_ADDRESS_1 = "http://103.204.192.187:8091/Service.asmx/";
//	public static String SOAP_ADDRESS = "http://103.204.192.187:8091/Service.asmx?WSDL";
	/**
	 * ADMS Local
	 */
//		public static String SOAP_ADDRESS = "http://103.204.192.187:8081/WWWebServices/Service.asmx?WSDL"; //local
//		public static String Report_Url = "http://103.204.192.187:8081/newcode/";
//		public static String SOAP_ADDRESS_1 = "http://103.204.192.187:8081/WWWebServices/Service.asmx/";

		/*public static String SOAP_ADDRESS = "http://192.168.1.50:8081/WWWebServices/Service.asmx?WSDL"; //local
		public static String Report_Url = "http://192.168.1.50:8081/newcode/";
		public static String SOAP_ADDRESS_1 = "http://192.168.1.50:8081/WWWebServices/Service.asmx/";*/
	// SOAP ACTION
	public static String Login_Action = NAMESPACE + "Login";
	public static String GetAnnouncements_Action = NAMESPACE
			+ "GetAnnouncements";
	public static String Mail_Get_FolderList_Action = NAMESPACE
			+ "Mail_Get_FolderList";
	public static String Mail_CreateMessageFolder_Action = NAMESPACE
			+ "Mail_CreateMessageFolder";
	public static String Mail_BindFolderTree_Action = NAMESPACE
			+ "Mail_BindFolderTree";
	public static String Mail_DeleteFolder_Action = NAMESPACE
			+ "Mail_DeleteFolder";
	public static String Mail_GetCommonInboxData_Action = NAMESPACE
			+ "Mail_GetCommonInboxData";  //
	public static String Mail_GetCommonSentData_Action = NAMESPACE
			+ "Mail_GetCommonSentData";
	public static String Mail_GetCommonDeletedData_Action = NAMESPACE
			+ "Mail_GetCommonDeletedData";
	public static String Mail_MarkAsReadUnread_Action = NAMESPACE
			+ "Mail_MarkAsReadUnread";
	public static String Mail_DeleteSelectedMessages_Action = NAMESPACE
			+ "Mail_DeleteSelectedMessages";
	public static String Mail_GetMessageById_Action = NAMESPACE
			+ "Mail_GetMessageById";
	public static String Mail_MoveMessageToFolder_Action = NAMESPACE
			+ "Mail_MoveMessageToFolder";
	public static String GetSiteList_Action = NAMESPACE
			+ "GetSiteList";
	public static String NewMail_Get_UserListBySite_Action = NAMESPACE
			+ "NewMail_Get_UserListBySite";
	public static String NewMail_SendMail_Action = NAMESPACE
			+ "NewMail_SendMail";
	public static String Get_InstrctListForMgrBySite_Action = NAMESPACE
			+ "Get_InstrctListForMgrBySite";
	public static String GetLevelList_Action = NAMESPACE
			+ "GetLevelList";
	public static String GetCurrentForAllInstrList_Action = NAMESPACE
			+ "GetCurrentForAllInstrList";
	public static String Insert_SwimCompCancellation_Action = NAMESPACE
			+ "Insert_SwimCompCancellation";
	public static String GetAttendanceList_Action = NAMESPACE
			+ "GetAttendanceList";
	public static String Insert_Attandance_Action = NAMESPACE
			+ "Insert_Attandance_LA";
	public static String GetISAAlert_Action = NAMESPACE
			+ "GetISAAlert";
	public static String GetStudentCommentsList_Action = NAMESPACE
			+ "GetStudentCommentsList";
	public static String DeleteStudentCommentByID_Action = NAMESPACE
			+ "DeleteStudentCommentByID";
	public static String InsertStudentComment_Action = NAMESPACE
			+ "InsertStudentComment";
	public static String ViewSchl_GetViewScheduleByDateAndInstrid_Action = NAMESPACE
			+ "ViewSchl_GetViewScheduleByDateAndInstrid";
	public static String Insert_Attandance_ForToday_Action = NAMESPACE
			+ "Insert_Attandance_ForToday_LA";
	public static String CheckTimeClicks_Action = NAMESPACE
			+ "CheckTimeClicks";
	public static String ProcessTimeClicks_Action = NAMESPACE
			+ "ProcessTimeClicks";
	public static String GetSales_Action = NAMESPACE
			+ "GetSales";
	public static String InsertProductDelievered_Action = NAMESPACE
			+ "InsertProductDelievered";
	public static String Get_InstrctListBySite_Action = NAMESPACE
			+ "Get_InstrctListBySite";
	public static String Insert_TerificTurbo_Action = NAMESPACE
			+ "Insert_TerificTurbo";
	public static String Insert_TurboFlash_Action = NAMESPACE
			+ "Insert_TurboFlash";
	public static String GetPoolList_Action = NAMESPACE
			+ "GetPoolList";
	public static String InsertDeckAssist_Action = NAMESPACE
			+ "InsertDeckAssist";
	public static String InsertDeckAssistUser_Action = NAMESPACE
			+ "InsertDeckAssistUser";
	public static String GetISAAlertBySite_Action = NAMESPACE
			+ "GetISAAlert";
	public static String GetAttendanceList_Multiple_Action = NAMESPACE
			+"GetAttendanceList_Multiple";
	public static String SOAP_Action_Insert_SendAttForReview= "http://tempuri.org/SendAttForReview";

	public static String GETISAAlertBySite1_Action = NAMESPACE+"GetISAAlertBySite";



	// SOAP METHOD
	public static String METHOD_NAME_SendAttForReview = "SendAttForReview";
	public static String Login_Method = "Login";
	public static String GetAnnouncements_Method = "GetAnnouncements";
	public static String Mail_Get_FolderList_Method = "Mail_Get_FolderList";
	public static String Mail_CreateMessageFolder_Method = "Mail_CreateMessageFolder";
	public static String Mail_BindFolderTree_Method = "Mail_BindFolderTree";
	public static String Mail_DeleteFolder_Method = "Mail_DeleteFolder";
	public static String Mail_GetCommonInboxData_Method = "Mail_GetCommonInboxData";
	public static String Mail_GetCommonSentData_Method = "Mail_GetCommonSentData";
	public static String Mail_GetCommonDeletedData_Method = "Mail_GetCommonDeletedData";
	public static String Mail_MarkAsReadUnread_Method = "Mail_MarkAsReadUnread";
	public static String Mail_DeleteSelectedMessages_Method = "Mail_DeleteSelectedMessages";
	public static String Mail_GetMessageById_Method = "Mail_GetMessageById";
	public static String Mail_MoveMessageToFolder_Method = "Mail_MoveMessageToFolder";
	public static String GetSiteList_Method = "GetSiteList";
	public static String NewMail_Get_UserListBySite_Method = "NewMail_Get_UserListBySite";
	public static String NewMail_SendMail_Method = "NewMail_SendMail";
	public static String Get_InstrctListForMgrBySite_Method = "Get_InstrctListForMgrBySite";
	public static String GetLevelList_Method = "GetLevelList";
	public static String GetCurrentForAllInstrList_Method = "GetCurrentForAllInstrList";
	public static String Insert_SwimCompCancellation_Method = "Insert_SwimCompCancellation";
	public static String GetAttendanceList_Method = "GetAttendanceList";
	public static String GetAttendanceList_Multiple_Method= "GetAttendanceList_Multiple";
	public static String Insert_Attandance_Method = "Insert_Attandance_LA";
	public static String GetISAAlert_Method = "GetISAAlert";
	public static String GetStudentCommentsList_Method = "GetStudentCommentsList";
	public static String DeleteStudentCommentByID_Method = "DeleteStudentCommentByID";
	public static String InsertStudentComment_Method = "InsertStudentComment";
	public static String ViewSchl_GetViewScheduleByDateAndInstrid_Method = "ViewSchl_GetViewScheduleByDateAndInstrid";
	public static String Insert_Attandance_ForToday_Method = "Insert_Attandance_ForToday_LA";
	public static String CheckTimeClicks_Method = "CheckTimeClicks";
	public static String ProcessTimeClicks_Method = "ProcessTimeClicks";
	public static String GetSales_Method = "GetSales";
	public static String InsertProductDelievered_Method = "InsertProductDelievered";
	public static String Get_InstrctListBySite_Method = "Get_InstrctListBySite";
	public static String Insert_TerificTurbo_Method = "Insert_TerificTurbo";
	public static String Insert_TurboFlash_Method = "Insert_TurboFlash";
	public static String GetPoolList_Method = "GetPoolList";
	public static String InsertDeckAssist_Method = "InsertDeckAssist";
	public static String InsertDeckAssistUser_Method = "InsertDeckAssistUser";
	public static String GetISAAlertBySite_Method = "GetISAAlert";
	public static String GETISAAlertBySite1_Method = "GetISAAlertBySite";

	//-----------------------------------------------------
	public static String SOAP_Action_Insert_SwimCompCancellation= "http://tempuri.org/Insert_SwimCompCancellation";
	public static String METHOD_NAME_Insert_SwimCompCancellation = "Insert_SwimCompCancellation";
	public static String METHOD_NAME_Insert_Attandance = "Insert_Attandance";
	public static String SOAP_Action_Insert_Attandance= "http://tempuri.org/Insert_Attandance";
	public static String METHOD_NAME_GetLevelList = "GetLevelList";
	public static String SOAP_Action_LevelList = "http://tempuri.org/GetLevelList";
	public static String METHOD_NAME_GetAttendanceList  = "GetAttendanceList";
	public static String SOAP_Action_AttendanceList =  "http://tempuri.org/GetAttendanceList";
	public static String METHOD_NAME_GETPOOLLIST = "GetPoolList";
	public static String SOAP_ACTION_POOLLIST = "http://tempuri.org/GetPoolList";
	public static String METHOD_NAME_InsertShadowRequest ="InsertShadowRequest";
	public static String METHOD_NAME_InsertShadowRequest_New1 ="InsertShadowRequest_New1";
	public static String METHOD_NAME_InsertShadowRequest_LA ="InsertShadowRequest_LA";
	public static String SOAP_Action_InsertShadowRequest = "http://tempuri.org/InsertShadowRequest";
	public static String SOAP_Action_InsertShadowRequest_New1 = "http://tempuri.org/InsertShadowRequest_New1";
	public static String SOAP_Action_InsertShadowRequest_LA = "http://tempuri.org/InsertShadowRequest_LA";
	public static String METHOD_NAME_GetUsersByType ="GetUsersByType";
	public static String SOAP_Action_GetUsersByType = "http://tempuri.org/GetUsersByType";
	public static String METHOD_NAME_InsertDeckAssist ="InsertDeckAssist";
	public static String SOAP_Action_InsertDeckAssistUser = "http://tempuri.org/InsertDeckAssistUser";
	public static String SOAP_Action_InsertDeckAssist = "http://tempuri.org/InsertDeckAssist";
	public static String METHOD_NAME_InsertDeckAssistUser ="InsertDeckAssistUser";
	public static String SOAP_Action_GetISAAlert = "http://tempuri.org/GetISAAlert";
	public static String METHOD_NAME_GetISAAlert = "GetISAAlert";
	public static String METHOD_NAME_GetSiteList ="Get_SiteList";
	public static String SOAP_Action_GetSiteList= "http://tempuri.org/Get_SiteList";
	public static String METHOD_NAME_CT_GetManagerListBySite ="CT_GetManagerListBySite";
	public static String METHOD_NAME_CT_SaveClockTick ="CT_SaveClockTick";


	public static String LoginName="";

}
