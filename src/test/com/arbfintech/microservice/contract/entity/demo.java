//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.arbfintech.framework.component.algorithm.core.LoanAlgorithm;
//import com.arbfintech.framework.component.core.constant.GlobalConst;
//import com.arbfintech.framework.component.core.constant.JsonKeyConst;
//import com.arbfintech.framework.component.core.enumerate.InstallmentAlgorithmTypeEnum;
//import com.arbfintech.framework.component.core.util.BigDecimalUtil;
//import com.arbfintech.framework.component.core.util.DateUtil;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static com.arbfintech.framework.component.algorithm.origination.PreOriginationAlgorithm.__findTargetApr;
//
//public class demo {
//
//
//
//    public static Double calculateApr(JSONObject optionJson, JSONArray installmentDataArray) {
//
//        Double apr = null;
//
//        Date beginDate = null;
//        Date previousDate = null;
//
//        List<Integer> diffDaysList = new ArrayList<Integer>();
//        List<Integer> cumsumList = new ArrayList<Integer>();
//
//        for (Object installmentDataObj : installmentDataArray) {
//            JSONObject installmentDataJson = (JSONObject) installmentDataObj;
//
//            Date effectiveDate = installmentDataJson.getDate(JsonKeyConst.INSTALLMENT_DATE);
//
//            if (beginDate == null) {
//                beginDate = effectiveDate;
//            } else {
//                Integer diffDays = DateUtil.diffDays(beginDate, effectiveDate);
//                cumsumList.add(diffDays);
//            }
//
//            if (previousDate != null) {
//                Integer diffDays = DateUtil.diffDays(previousDate, effectiveDate);
//                diffDaysList.add(diffDays);
//            }
//
//            previousDate = effectiveDate;
//        }
//
//        System.out.println(JSON.toJSONString(diffDaysList));
//        System.out.println(diffDaysList.size());
//
//        System.out.println(JSON.toJSONString(cumsumList));
//        System.out.println(cumsumList.size());
//
//        Double startApr = 5.0;
//        Double stopApr = 8.0;
//
//        Double bisectionApr = 0.0;
//        while ((stopApr - startApr) >= 0.000001) {
//            bisectionApr = (startApr + stopApr) / 2;
//
//            Double bisectionResult = __findTargetApr(optionJson, installmentDataArray, cumsumList, bisectionApr);
//            if (bisectionResult <= 0.00005 && bisectionResult > 0) {
//                break;
//            }
//
//            Double startResult = __findTargetApr(optionJson, installmentDataArray, cumsumList, startApr);
//            if (bisectionResult * startResult < 0) {
//                stopApr = bisectionApr;
//            } else {
//                startApr = bisectionApr;
//            }
//        }
//
//        apr = BigDecimalUtil.roundHalfUp(bisectionApr, 6).doubleValue();
//
//        return apr;
//    }
//
//
//
//
//    public JSONObject generateLoanContract(JSONObject optionJson) {
//
//        String begin = optionJson.getString(JsonKeyConst.BEGIN);
//        Date beginDate = DateUtil.str2date(begin);
//        Date endDate = DateUtil.addDays(beginDate, GlobalConst.RUNTIME_DAYS_OF_YEAR * 2);
//        String end = DateUtil.date2str(endDate);
//
//        Integer algorithmType = InstallmentAlgorithmTypeEnum.INTEREST_FIRST.getValue();
//
//        optionJson.put(JsonKeyConst.END, end);
//        optionJson.put(JsonKeyConst.ALGORITHM_TYPE, algorithmType);
//        optionJson.put(JsonKeyConst.HAS_CREDIT, true);
//        optionJson.put(JsonKeyConst.MIN_DAYS_OF_START_CALC_INTEREST, GlobalConst.RUNTIME_MIN_DAYS_OF_START_CALC_INTEREST);
//
//        JSONArray holidayArray = holidayRestService.listHolidayByRange(beginDate.getTime(), endDate.getTime());
//
//        JSONArray installmentDateArray = LoanAlgorithm.calculateInstallmentDateArray(optionJson, holidayArray);
//        //logger.info("installmentDateArray:" + installmentDateArray.toJSONString());
//        end = installmentDateArray.getJSONObject(installmentDateArray.size() - 1).getString(JsonKeyConst.DATE);
//
//        Date firstDueDate = installmentDateArray.getJSONObject(0).getDate(JsonKeyConst.DATE);
//        Date maturityDate = installmentDateArray.getJSONObject(installmentDateArray.size() - 1).getDate(JsonKeyConst.DATE);
//
//        JSONObject summaryJson = LoanAlgorithm.calculateInstallmentSummary(optionJson, installmentDateArray);
//        //logger.info("summaryJson:" + summaryJson.toJSONString());
//        JSONArray installmentDataArray = summaryJson.getJSONArray(JsonKeyConst.INSTALLMENT_LIST);
//        Double apr = LoanAlgorithm.calculateApr(optionJson, installmentDataArray);
//
//        JSONObject contractJson = new JSONObject();
//        contractJson.put(JsonKeyConst.FIRST_INSTALLMENT_DATE, firstDueDate.getTime());
//        contractJson.put(JsonKeyConst.LAST_INSTALLMENT_DATE, maturityDate.getTime());
//        contractJson.put(JsonKeyConst.ANNUAL_PERCENTAGE_RATE, apr);
//        contractJson.put(JsonKeyConst.TOTAL_INTEREST, summaryJson.get(JsonKeyConst.TOTAL_INTEREST));
//        contractJson.put(JsonKeyConst.TOTAL_AMOUNT, summaryJson.get(JsonKeyConst.TOTAL_AMOUNT));
//        contractJson.put(JsonKeyConst.REGULAR_AMOUNT, summaryJson.get(JsonKeyConst.REGULAR_AMOUNT));
//        contractJson.put(JsonKeyConst.INSTALLMENT_LIST, installmentDataArray);
//
//        return contractJson;
//    }
//
//
//
//
//
//
//
//
//
//
//
//}
