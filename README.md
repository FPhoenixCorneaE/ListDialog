# ListDialog
列表弹窗


特性：

	一行代码设置弹窗重力位置、宽高、背景、圆角、分割线、字体大小、选中与非选中颜色、进入退出动画、点击监听等；


	
	
代码示例：
		if (listDialog == null) {
                    listDialog = new ListDialog.Builder(this)
                            .setDatas(new String[]{"默认", "从上到下", "从下到上", "由高到低", "由低到高"})
//                            .setDialogWidth(ViewUtils.dp2px(this,300))
//                            .setDialogHeight(ViewUtils.dp2px(this,400))
                            .setDialogBackground(Color.CYAN, 10f)
                            .setDialogGravity(Gravity.BOTTOM)
                            .setDialogMargins(15, 5, 15, 5)
                            .setDividerDrawable(new ColorDrawable(Color.RED))
                            .setDialogEnterAnimation(R.anim.dialog_enter)
                            .setDialogExitAnimation(R.anim.dialog_exit)
//                            .setTextGravity(Gravity.START | Gravity.CENTER_VERTICAL)
//                            .setTextPadding(15, 0, 0, 0)
                            .setTextSize(15f)
                            .setTextNormalColor(Color.argb(40, 230, 152, 98))
                            .setTextSelectedColor(Color.argb(70, 130, 52, 198))
                            .setItemHeight(48f)
                            .setSelectedPosition(2)
                            .setOnItemClickListener(new ListDialog.OnItemClickListener() {
                                @Override
                                public void onItemClick(Dialog dialog, List<String> datas, int position) {
                                    Toast.makeText(MainActivity.this, datas.get(position), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
                }
                listDialog.show();


