package com.jkmall.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;

@CanalEventListener
public class CanalDataEventListener {

    @InsertListenPoint
    public void onInsertEventPoint(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        rowData.getAfterColumnsList().stream().
                forEach(column -> System.out.println("Insert Row "+column.getName()+" :: "+column.getValue()));
    }
}
