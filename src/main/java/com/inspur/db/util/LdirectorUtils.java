package com.inspur.db.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author WX0liucy
 */
public class LdirectorUtils {
    private LdirectorUtils() {
    }

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(LdirectorUtils.class);

    /**
     * when health check is on, add listener
     *
     * @param lDirectorItems Items of lDirector.cf
     * @return Boolean whether to add successfully
     */
    public static Boolean addListenerToDirectorCf(Map<String, String> lDirectorItems) {
        StringBuilder vipServer = new StringBuilder(lDirectorItems.get(ConfiguresUtil.VIP))
                .append(":")
                .append(lDirectorItems.get(ConfiguresUtil.VIP_PORT));
        try (FileWriter fw = new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG, true)) {
            StringBuilder initMsgBuilder = new StringBuilder("\r" + ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer + "\n")
                    .append(addCfgItems(lDirectorItems));
            if (ConfiguresUtil.L_DIRECTOR_CHECK_TYPE_STATUS_NEGOTIATE.equals(lDirectorItems.get(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE))) {
                initMsgBuilder.append("\t")
                        .append(ConfiguresUtil.L_DIRECTOR_REQUEST)
                        .append(ConfiguresUtil.EQUAL_SIGN)
                        .append("\"")
                        .append(lDirectorItems.get(ConfiguresUtil.L_DIRECTOR_REQUEST))
                        .append("\"")
                        .append("\n");
            }
            fw.write(initMsgBuilder.toString());
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
        return true;
    }

    /**
     * Add a real server
     *
     * @param vip                  vip
     * @param vipPort              vipPort
     * @param realServerWithWeight ip+port+gate+weight
     * @return boolean whether to add successfully
     */
    public static boolean addRealServerToDirectorCf(String vip, String vipPort, String realServerWithWeight) {
        StringBuilder vipServer = new StringBuilder(vip)
                .append(":")
                .append(vipPort);
        try (BufferedReader br = new BufferedReader(new FileReader(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
            StringBuilder contentBuilder = new StringBuilder();
            if (realServerWithWeight == null) {
                return false;
            }
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
                if (currentLine.startsWith(ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer)) {
                    contentBuilder.append("\t")
                            .append(ConfiguresUtil.L_DIRECTOR_REAL)
                            .append(ConfiguresUtil.EQUAL_SIGN)
                            .append(realServerWithWeight)
                            .append("\n");
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
                bw.write(contentBuilder.toString());
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * delete a real server
     *
     * @param vip        vip
     * @param vipPort    vipPort
     * @param realServer ip+port
     * @return result  whether to delete successfully
     */
    public static boolean deleteRealServerFromDirectorCf(String vip, String vipPort, String realServer) {
        StringBuilder vipServer = new StringBuilder(vip)
                .append(":")
                .append(vipPort);
        StringBuilder realServerItem = new StringBuilder(ConfiguresUtil.L_DIRECTOR_REAL)
                .append(ConfiguresUtil.EQUAL_SIGN)
                .append(realServer);
        try (BufferedReader br = new BufferedReader(new FileReader(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
            StringBuilder contentBuilder = new StringBuilder();
            String currentLine;
            boolean bRet = false;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().equals(ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer)) {
                    LOG.info("Found the Appoint vip: " + vipServer);
                    bRet = true;
                }
                if (bRet && currentLine.trim().startsWith(realServerItem.toString())) {
                    bRet = false;
                    continue;
                }
                contentBuilder.append(currentLine).append("\n");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
                bw.write(contentBuilder.toString());
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * update configuration items
     *
     * @param parametersDateMap parameters map
     * @return boolean  whether to update successfully
     */
    public static boolean updateCfgItemFromDirectorCf(Map<String, String> parametersDateMap) {
        StringBuilder vipServer = new StringBuilder(parametersDateMap.get("vip"))
                .append(":")
                .append(parametersDateMap.get("vipPort"));
        try (BufferedReader br = new BufferedReader(new FileReader(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
            StringBuilder contentBuilder = new StringBuilder();
            updateCfgItem(br, vipServer, contentBuilder, parametersDateMap);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
                bw.write(contentBuilder.toString());
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * update configuration items
     *
     * @param br                buffered reader
     * @param vipServer         vip+port
     * @param contentBuilder    read file contents
     * @param parametersDateMap parameters map
     */

    private static void updateCfgItem(BufferedReader br, StringBuilder vipServer,
                                      StringBuilder contentBuilder, Map<String, String> parametersDateMap) {
        try {
            String currentLine;
            boolean bRet = false;
            Map<String, Boolean> parametersMap = getParametersMap();

            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().equals(ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer)) {
                    LOG.info("Found the Appoint vip");
                    for (String parameter : parametersMap.keySet()) {
                        parametersMap.put(parameter, true);
                    }
                    if (parametersDateMap.get(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE).equals(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE_STATUS_CONNECT)) {
                        parametersMap.put(ConfiguresUtil.L_DIRECTOR_REQUEST, false);
                    }
                }
                bRet = forCfgItem(bRet, parametersMap, parametersDateMap, currentLine, contentBuilder);

                if (bRet) {
                    bRet = false;
                    continue;
                }
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * @param bRet              bRet
     * @param parametersMap     Change the status of an item
     * @param parametersDateMap Data to be changed
     * @param currentLine       current Line
     * @param contentBuilder    Store read data
     * @return boolean
     */
    private static boolean forCfgItem(boolean bRet, Map<String, Boolean> parametersMap, Map<String, String> parametersDateMap, String currentLine, StringBuilder contentBuilder) {
        StringBuilder negotiateStatus = new StringBuilder(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE)
                .append(ConfiguresUtil.EQUAL_SIGN)
                .append(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE_STATUS_NEGOTIATE);
        for (Map.Entry<String, Boolean> entry : parametersMap.entrySet()) {
            boolean whetherUpdateConfigItem = Boolean.TRUE.equals(parametersMap.get(entry.getKey())) && (currentLine.trim().startsWith(entry.getKey()) || currentLine.trim().startsWith("request"));
            if (whetherUpdateConfigItem) {
                parametersMap.put(entry.getKey(), false);
                String checkType = "\t" + entry.getKey() + "=" + parametersDateMap.get(entry.getKey()) + "\n";
                String replace = currentLine.replace(currentLine, checkType);
                //现在negotiate，原来connect  现在request有值  原来的request没有值
                if (parametersMap.get(ConfiguresUtil.L_DIRECTOR_REQUEST) != null && !currentLine.trim().startsWith(ConfiguresUtil.L_DIRECTOR_REQUEST)) {
                    contentBuilder.append(replace);
                }
                //现在negotiate，原来connect
                if (checkType.trim().equals(negotiateStatus.toString())) {
                    parametersMap.put(ConfiguresUtil.L_DIRECTOR_REQUEST, false);
                    String request = "\t" + ConfiguresUtil.L_DIRECTOR_REQUEST + "=" + "\"" + parametersDateMap.get(ConfiguresUtil.L_DIRECTOR_REQUEST) + "\"" + "\n";
                    contentBuilder.append(request);
                }
                bRet = true;
            }
        }
        return bRet;
    }


    /**
     * delete all configuration items of the current listener when health check is turned off
     *
     * @param vip     vip
     * @param vipPort vipPort
     * @return boolean  whether to delete successfully
     */
    public static boolean deleteListenerFromDirectorCf(String vip, String vipPort) {
        StringBuilder vipServer = new StringBuilder(vip)
                .append(":")
                .append(vipPort);
        try (BufferedReader br = new BufferedReader(new FileReader(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
            StringBuilder contentBuilder = new StringBuilder();
            String currentLine;
            boolean bRet = false;
            while ((currentLine = br.readLine()) != null) {
                boolean whetherVipServer = currentLine.trim().equals(ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer);
                if (whetherVipServer) {
                    LOG.info("Found the Appoint vip");
                    bRet = true;
                }
                if (bRet && currentLine.trim().startsWith(ConfiguresUtil.L_DIRECTOR_VIRTUAL) && !whetherVipServer) {
                    bRet = false;
                }
                if (bRet) {
                    continue;
                }
                contentBuilder.append(currentLine).append("\n");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
                bw.write(contentBuilder.toString());
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * update real server
     *
     * @param vip                  vip
     * @param vipPort              vipPort
     * @param realServerWithWeight ip+server+weight
     * @return boolean  whether to update successfully
     */
    public static boolean updateRealServerForDirectorCf(String vip, String vipPort, String realServerWithWeight) {
        StringBuilder vipServer = new StringBuilder(vip)
                .append(":")
                .append(vipPort);
        StringBuilder realServerItem = new StringBuilder("\t")
                .append(ConfiguresUtil.L_DIRECTOR_REAL)
                .append(ConfiguresUtil.EQUAL_SIGN)
                .append(realServerWithWeight);
        String[] split = realServerWithWeight.split(" ");
        try (BufferedReader br = new BufferedReader(new FileReader(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
            StringBuilder contentBuilder = new StringBuilder();
            String currentLine;
            boolean bRet = false;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().equals(ConfiguresUtil.L_DIRECTOR_VIRTUAL + ConfiguresUtil.EQUAL_SIGN + vipServer)) {
                    LOG.info("Found the Appoint vip");
                    bRet = true;
                }
                if (bRet && currentLine.trim().contains(split[0])) {
                    bRet = false;
                    String replace = currentLine.replace(currentLine, realServerItem);
                    contentBuilder.append(replace).append("\n");
                    continue;
                }
                contentBuilder.append(currentLine).append("\n");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ConfiguresUtil.PATH_OF_L_DIRECTOR_CONFIG))) {
                bw.write(contentBuilder.toString());
            }
        } catch (IOException e) {
            LOG.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * get the map of modified parameters. The value of map is false
     *
     * @return Map<String   ,       Boolean>                                                               ,                                                                                                                               Boolean>
     */
    public static Map<String, Boolean> getParametersMap() {
        HashMap<String, Boolean> parameters = new HashMap<>(10);
        parameters.put(ConfiguresUtil.L_DIRECTOR_CHECK_TIME_OUT, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_CHECK_INTERVAL, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_FAILURE_COUNT, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_SCHEDULER, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_CHECK_PORT, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE, false);
        parameters.put(ConfiguresUtil.L_DIRECTOR_REQUEST, false);
        return parameters;
    }

    /**
     * Get the parameter map required by ldirector
     *
     * @param objRecv objRecv
     * @return Map<String   ,       String>
     */
    public static Map<String, String> getLdItemsMap(JSONObject objRecv) {
        HashMap<String, String> directorItems = new HashMap<>(11);
        String checkType = objRecv.getString(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE);
        directorItems.put(ConfiguresUtil.VIP, objRecv.getString(ConfiguresUtil.VIP));
        directorItems.put(ConfiguresUtil.VIP_PORT, objRecv.getString(ConfiguresUtil.PORT));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_SCHEDULER, objRecv.getString(ConfiguresUtil.RULE));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_PROTOCOL, objRecv.getString(ConfiguresUtil.L_DIRECTOR_PROTOCOL));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_CHECK_TIME_OUT, objRecv.getString(ConfiguresUtil.L_DIRECTOR_CHECK_TIME_OUT));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_CHECK_INTERVAL, objRecv.getString(ConfiguresUtil.L_DIRECTOR_CHECK_INTERVAL));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_FAILURE_COUNT, objRecv.getString(ConfiguresUtil.L_DIRECTOR_FAILURE_COUNT));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_CHECK_PORT, objRecv.getString(ConfiguresUtil.L_DIRECTOR_CHECK_PORT));
        directorItems.put(ConfiguresUtil.L_DIRECTOR_CHECK_TYPE, checkType);
        if (ConfiguresUtil.L_DIRECTOR_CHECK_TYPE_STATUS_NEGOTIATE.equals(checkType)) {
            directorItems.put(ConfiguresUtil.L_DIRECTOR_REQUEST, objRecv.getString(ConfiguresUtil.L_DIRECTOR_REQUEST));
        }
        return directorItems;
    }

    /**
     * Get the connection string of RealServer
     *
     * @param objRecv Incoming JSON data
     * @return string
     */
    public static String getRealServer(JSONObject objRecv) {
        StringBuilder realServer = new StringBuilder();
        realServer.append(objRecv.getString(ConfiguresUtil.BACKEND_IP))
                .append(ConfiguresUtil.PORT_SEPARATOR)
                .append(objRecv.getString(ConfiguresUtil.BACKEND_PORT));

        if (objRecv.getString(ConfiguresUtil.BACKEND_WEIGHT) != null) {
            realServer.append(ConfiguresUtil.SPACE_SEPARATOR)
                    .append(ConfiguresUtil.DR_GATE)
                    .append(ConfiguresUtil.SPACE_SEPARATOR)
                    .append(objRecv.getString(ConfiguresUtil.BACKEND_WEIGHT));
        }
        return realServer.toString();
    }

    /**
     * Loop add configuration item
     *
     * @param lDirectorItems configuration items
     * @return string
     */
    public static String addCfgItems(Map<String, String> lDirectorItems) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : lDirectorItems.entrySet()) {
            if ("vip".equals(entry.getKey()) || "vipPort".equals(entry.getKey()) || "request".equals(entry.getKey())) {
                continue;
            }
            stringBuilder.append("\t")
                    .append(entry.getKey())
                    .append(ConfiguresUtil.EQUAL_SIGN)
                    .append(entry.getValue())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}








