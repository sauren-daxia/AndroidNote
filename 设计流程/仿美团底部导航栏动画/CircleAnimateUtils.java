public class CircleAnimateUtils {

    public static void handleAnimate(final View animateView) {
        Animator animator = CircularRevealLayout.Builder.on(animateView)
                .centerX(animateView.getWidth() / 2)
                .centerY(animateView.getHeight() / 2)
                .startRadius(0)
                .endRadius((float) Math.hypot(animateView.getWidth(), animateView.getHeight()))
                .create();

        mAnimator.setDuration(5000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());      
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                animateView.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
     }
 }