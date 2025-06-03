package com.aiyuns.tinkerplay.Algorithm;

import java.io.Serializable;

/**
 * @Author: aiYunS @Date: 2022-11-17 16:09 @Description: 分页算法
 */
public class PagingAlgorithm implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 每页默认的项数(10) */
  public static final int DEFAULT_ITEMS_PER_PAGE = 10;

  /** 滑动窗口默认的大小(7) */
  public static final int DEFAULT_SLIDER_SIZE = 7;

  /** 表示项数未知(<code>Integer.MAX_VALUE</code>) */
  public static final int UNKNOWN_ITEMS = Integer.MAX_VALUE;

  /** 状态量 */
  // 当前页码
  private int page;

  // 总共项数
  private int items;
  // 每页项数
  private int itemsPerPage;
  // 开始条数
  private int startRow;
  // 结束条数
  private int endRow;

  /** 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，默认每页显示<code>10</code>项 */
  public PagingAlgorithm() {
    this(0);
  }

  /**
   * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，指定每页项数
   *
   * @param itemsPerPage 每页项数。
   */
  public PagingAlgorithm(int itemsPerPage) {
    this(itemsPerPage, UNKNOWN_ITEMS);
  }

  /**
   * 创建一个分页器，初始项数为无限大<code>UNKNOWN_ITEMS</code>，指定每页项数
   *
   * @param itemsPerPage 每页项数
   * @param items 总项数
   */
  public PagingAlgorithm(int itemsPerPage, int items) {
    this.items = (items >= 0) ? items : 0;
    this.itemsPerPage = (itemsPerPage > 0) ? itemsPerPage : DEFAULT_ITEMS_PER_PAGE;
    this.page = calcPage(0);
  }

  /**
   * 取得总页数。
   *
   * @return 总页数
   */
  public int getPages() {
    if (items <= 0) {
      return 1;
    }
    return (int) Math.ceil((double) items / itemsPerPage);
  }

  /**
   * 取得当前页。
   *
   * @return 当前页
   */
  public int getPage() {
    return page;
  }

  /**
   * 设置并取得当前页
   *
   * @param page 当前页
   * @return 设置后的当前页
   */
  public int setPage(int page) {
    return (this.page = calcPage(page));
  }

  /**
   * 取得总项数。
   *
   * @return 总项数
   */
  public int getItems() {
    return items;
  }

  /**
   * 设置并取得总项数。如果指定的总项数小于0，则被看作0 总项数
   *
   * @param items
   * @return 设置以后的总项数
   */
  public int setItems(int items) {
    this.items = (items >= 0) ? items : 0;
    setPage(page);
    return this.items;
  }

  /**
   * 取得每页项数。
   *
   * @return 每页项数
   */
  public int getItemsPerPage() {
    return itemsPerPage;
  }

  /**
   * 设置并取得每页项数。如果指定的每页项数小于等于0，则使用默认值<code>DEFAULT_ITEMS_PER_PAGE</code> 并调整当前页使之在改变每页项数前后显示相同的项
   *
   * @param itemsPerPage 每页项数
   * @return 设置后的每页项数
   */
  public int setItemsPerPage(int itemsPerPage) {
    int tmp = this.itemsPerPage;
    this.itemsPerPage = (itemsPerPage > 0) ? itemsPerPage : DEFAULT_ITEMS_PER_PAGE;
    if (page > 0) {
      setPage((int) (((double) (page - 1) * tmp) / this.itemsPerPage) + 1);
    }
    return this.itemsPerPage;
  }

  /**
   * 取得当前页的长度，即当前页的实际项数。相当于 <code>endIndex() - beginIndex() + 1</code>
   *
   * @return 当前页的长度
   */
  public int getLength() {
    if (page > 0) {
      return Math.min(itemsPerPage * page, items) - (itemsPerPage * (page - 1));
    } else {
      return 0;
    }
  }

  /**
   * 取得首页页码。
   *
   * @return 首页页码
   */
  public int getFirstPage() {
    return calcPage(1);
  }

  /**
   * 取得末页页码。
   *
   * @return 末页页码
   */
  public int getLastPage() {
    return calcPage(getPages());
  }

  /**
   * 取得前一页页码。
   *
   * @return 前一页页码
   */
  public int getPreviousPage() {
    return calcPage(page - 1);
  }

  /**
   * 取得前n页页码
   *
   * @param n 前n页
   * @return 前n页页码
   */
  public int getPreviousPage(int n) {
    return calcPage(page - n);
  }

  /**
   * 取得后一页页码。
   *
   * @return 后一页页码
   */
  public int getNextPage() {
    return calcPage(page + 1);
  }

  /**
   * 取得后n页页码。
   *
   * @param n 后n面
   * @return 后n页页码
   */
  public int getNextPage(int n) {
    return calcPage(page + n);
  }

  /**
   * 判断指定页码是否被禁止，也就是说指定页码超出了范围或等于当前页码。
   *
   * @param page 页码
   * @return boolean 是否为禁止的页码
   */
  public boolean isDisabledPage(int page) {
    return ((page < 1) || (page > getPages()) || (page == this.page));
  }

  /**
   * 计算页数，但不改变当前页。
   *
   * @param page 页码
   * @return 返回正确的页码(保证不会出边界)
   */
  protected int calcPage(int page) {
    int pages = getPages();

    if (pages > 0) {
      return (page < 1) ? 1 : ((page > pages) ? pages : page);
    }

    return 0;
  }

  /**
   * 创建复本。
   *
   * @return 复本
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (java.lang.CloneNotSupportedException e) {
      // 不可能发生
      return null;
    }
  }

  /**
   * @param startRow the startRow to set
   */
  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  /**
   * @param endRow the endRow to set
   */
  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }

  /**
   * @return the startRow
   */
  public int getStartRow() {
    if (page > 0) {
      startRow = (itemsPerPage * (page - 1)) + 1;
    } else {
      startRow = 0;
    }
    return startRow;
  }

  /**
   * @return the endRow
   */
  public int getEndRow() {
    if (page > 0) {
      endRow = Math.min(itemsPerPage * page, items);
    } else {
      endRow = 0;
    }
    return endRow;
  }
}
