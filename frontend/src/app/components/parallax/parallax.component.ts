import { CommonModule } from '@angular/common';
import { Component, Input, NgZone } from '@angular/core';

@Component({
  selector: 'app-parallax',
  imports: [CommonModule],
  templateUrl: './parallax.component.html',
  styleUrls: ['./parallax.component.css'],
})
export class ParallaxComponent {
  @Input() imgSrc: string = '';
  @Input() depth: number = 0;

  parallaxTranslation: number = 0;
  // parallaxTranslation: Be

  private animationFrameId: number | null = null;
  private skip = false;

  // constructor(private ngZone: NgZone) {}

  ngOnInit() {
    // this.ngZone.runOutsideAngular(() => {
    //   this.startAnimationLoop();
    // });
    this.startAnimationLoop();
  }

  startAnimationLoop() {
    const animate = () => {
      // console.log(this.skip);

      this.skip = !this.skip;
      if (!this.skip) {
        this.setParallaxTranslation(0.03 * this.depth);

      };
      
      this.animationFrameId = requestAnimationFrame(animate);
    };
    this.animationFrameId = requestAnimationFrame(animate);
  }

  ngOnDestroy() {
    if (this.animationFrameId !== null) {
      cancelAnimationFrame(this.animationFrameId);
    }
  }

  

  getParallaxTranslation() {
    return this.parallaxTranslation;
  }

  setParallaxTranslation(increment: number) {
    this.parallaxTranslation = (this.parallaxTranslation + increment) % 100;
  }

  getHeight() {
    return this.depth * 5;
  }
}
