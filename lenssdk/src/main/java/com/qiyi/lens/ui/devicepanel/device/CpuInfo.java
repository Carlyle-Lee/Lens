package com.qiyi.lens.ui.devicepanel.device;

class CpuInfo {
    // total
    int cpu;
    int idle;
    int iowait;

    private int parseSafely(String var, String key) {

        if (var == null) return 0;
        try {
            int index = var.indexOf(key);
            if (index > 0) {
                return Integer.parseInt(var.substring(0, index));
            }
        } catch (NumberFormatException e) {
            // do nothing
        }
        return 0;
    }

    public CpuInfo(String line) {
        String[] ar = line.split(" ");
        if (ar.length > 4) {
            cpu = parseSafely(ar[0], "%cpu");
            idle = parseSafely(ar[3], "%idle");
            iowait = parseSafely(ar[4], "%iow");
        }
    }

    public String getCpuRate() {
        int cpuCount = cpu / 100;
        return ((cpu - idle) / cpuCount) + "% * " + cpuCount;
    }

    public String getIORate() {
        int cpuCount = cpu / 100;
        return (iowait / cpuCount) + "%";
    }

}