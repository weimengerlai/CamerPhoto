# CamerPhoto
实现全部封装拍照和相册选取照片，以及二次采样图片，在TestActivity测试类里面返回了需要向服务器提交的经过二次从采样的图片。


跳转到相册获取照片的代码（你只需要把该项目作为model加入你的项目）
 Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivityForResult(intent, RESULT_CODE);


 //获取拍照后储存的路径（点击拍照的时候需要调用的代码）
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = PhotoApplication.getImgDir();

        file.mkdirs();

        mCurrentPhotoPath = file + "/" + timeStamp + ".png";
        return new File(mCurrentPhotoPath);
    }


//在activityresult里面图片的路径全部被返回（返回的是二次采样过的图片）
//返回图片的数量
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CODE) {
            if (resultCode == REQUEST_CODE) {//相册获、取照片的方法
                ArrayList<ImageUrl> imageUrlArrayList1 = (ArrayList<ImageUrl>) data.getSerializableExtra("data");
                imageUrlArrayList.addAll(imageUrlArrayList1);
                tv_activity_test.setText("图片的数量为" + imageUrlArrayList.size() + "张");
            } else if (resultCode == RESULT_OK) {//拍照获取图片

                showProgressDialog();
                ExecutorService executorService = Executors.newFixedThreadPool(6);
                executorService.submit(new DownImageThread(mCurrentPhotoPath, handler, imageUrlArrayList.size()));

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    //点击看大图的时候需要调用的代码
     Intent intent = new Intent(context, LookBigImage.class);
                    intent.putExtra(LookBigImage.LOOK_BIG_IMAGE_LIST, (Serializable) imageUrlList);
                    intent.putExtra(LookBigImage.LOOK_BIG_IMAGE_POSITION,position);
                    context.startActivity(intent);
看大图的功能实现了图片的三级缓存功能解决的oom问题只要调用即可。
