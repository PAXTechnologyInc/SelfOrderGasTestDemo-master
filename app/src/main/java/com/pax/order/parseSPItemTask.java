package com.pax.order;

import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsItem;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.orderserver.entity.getitem.Attribute;
import com.pax.order.orderserver.entity.getitem.AttributeCategorie;
import com.pax.order.orderserver.entity.getitem.AttributeValue;
import com.pax.order.orderserver.entity.getitem.Item;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyr on 2019/1/15.
 */

public class parseSPItemTask implements Runnable {
    private static final String TAG = "parseSPItemTask";
    private List<Item> mSPItemList = new ArrayList<>();

    public parseSPItemTask(List<Item> spItemList) {
        mSPItemList.addAll(spItemList);
    }

    @Override
    public void run() {

        List<GoodsItem> goodsItemList = new ArrayList<>();
        for (Item item : mSPItemList) {
            GoodsItem goodsItem = new GoodsItem();
            goodsItem.setId(Integer.parseInt(item.getId()));

            // many filed will return null [TO DO]
            if (null == item.getStock() || "".equals(item.getStock())) {
                if (null == item.getInventoryMethod() || "".equals(item.getInventoryMethod())) {
                    goodsItem.setStock(Integer.MAX_VALUE);
                } else {
                    goodsItem.setStock(0);
                }
            } else {
                goodsItem.setStock(Integer.parseInt(item.getStock()));
            }
            if(null == item.getNotifyStock() || "".equals(item.getNotifyStock())){
                goodsItem.setNotifyStock(0);
            }else{
                goodsItem.setNotifyStock(Integer.parseInt(item.getNotifyStock()));
            }

            goodsItem.setName(item.getName());
            goodsItem.setOpen(Boolean.parseBoolean(item.getOpen()));
            goodsItem.setPrice(Double.parseDouble(item.getPrice()));
            if ((null == item.getTaxAmt()) || ("".equals(item.getTaxAmt()))) {
                goodsItem.setTaxAmount(0);
            } else {
                goodsItem.setTaxAmount(Double.parseDouble(item.getTaxAmt()));
            }

            int categoryId = Integer.parseInt((null == item.getCategoryId() || "".equals(item
                    .getCategoryId())) ? "0" : item.getCategoryId());
            goodsItem.setCategoryId(categoryId);
            goodsItem.setCategoryName(item.getCategoryName());
            goodsItem.setInventoryMethod(item.getInventoryMethod());
            goodsItem.setDescription(item.getDescription());

            String picUrl = item.getPicUrl();
            if (null == picUrl || "".equals(picUrl)) {
                goodsItem.setPicUrl("");
            } else {
                goodsItem.setPicUrl(picUrl);
            }

            List<GoodsAttributes> goodsAttributesList = new ArrayList<>();
            for (Attribute attribute : item.getAttributes()) {
                GoodsAttributes goodsAttributes = new GoodsAttributes();
                goodsAttributes.setId(attribute.getId());
                goodsAttributes.setName(attribute.getName());
                goodsAttributes.setPirceAdjustMent(Double.parseDouble(attribute
                        .getPirceAdjustMent()));
                goodsAttributes.setTaxAdjustment(Double.parseDouble(attribute.getTaxAdjustment()));
                if (null == attribute.getStock() || "".equals(attribute.getStock())) {
                    goodsAttributes.setStock(0);
                } else {
                    goodsAttributes.setStock(Integer.parseInt(attribute.getStock()));
                }
                if(null == attribute.getNotifyStock() || "".equals(attribute.getNotifyStock())){
                    goodsAttributes.setmNotifyStock(0);
                }else{
                    goodsAttributes.setmNotifyStock(Integer.parseInt(attribute.getNotifyStock()));
                }
                goodsAttributes.setAttributeValue(attribute.getAttributeValue());
                goodsAttributesList.add(goodsAttributes);

            }
            goodsItem.setGoodsAttributesList(goodsAttributesList);
            goodsItem.setmAttCate(item.getAttributeCategories());

            goodsItemList.add(goodsItem);
        } // end of for

        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setGoodsItemList(goodsItemList);
        processMessage.setMessageCode(MessgeCode.DOWNITEMSUCC);
        EventBus.getDefault().post(processMessage);
    }
}
