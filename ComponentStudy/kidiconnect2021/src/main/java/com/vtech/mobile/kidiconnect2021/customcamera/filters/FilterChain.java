package com.vtech.mobile.kidiconnect2021.customcamera.filters;

import java.util.List;

public class FilterChain {

    public FilterContext filterContext;
    private List<AbstractFilter> filters;
    private int index;

    private boolean pause = false;

    public FilterChain(FilterContext filterContext, List<AbstractFilter> filters, int index) {
        this.filterContext = filterContext;
        this.filters = filters;
        this.index = index;
    }

    public int process(int textureId){
        if (index>=filters.size()){
            return textureId;
        }
        if (pause){
            return textureId;
        }

        FilterChain nextFilterChain = new FilterChain(filterContext, filters, index + 1);
        AbstractFilter abstractFilter = filters.get(index);
        return abstractFilter.onDraw(textureId,nextFilterChain);
    }

    public void setSize(int width, int height) {
        filterContext.setSize(width, height);
    }

    public void setTransformMatrix(float[] mtx) {
        filterContext.setTransformMatrix(mtx);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void release(){
        for (AbstractFilter filter : filters) {
            filter.release();
        }
    }
}
