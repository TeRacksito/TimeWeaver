// @ts-nocheck
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ParallaxComponent } from '../parallax/parallax.component';
import { tick } from '@angular/core/testing';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule, ParallaxComponent],
  templateUrl: './game.component.html',
  styleUrl: './game.component.css',
})
export class GameComponent {
  images: string[];

  barStrength = 0;
  barPrecision = 0;
  attempts = 0;
  barUp = true;

  public users = JSON.parse(localStorage.getItem('users')) ?? [];

  bagRestPosition = { x: 100, y: 400 };
  cartPosition = { x: 450, y: 380 };

  bagPosition = { x: 0, y: 0 };
  targetPosition = { x: 0, y: 0 };

  animationFrames = 20;
  currentFrame = 0;

  bagLaunched = false;
  cooldown = false;
  hiddenForm = true;

  punctuation = 0;
  lastPunctuation = 0;

  private skip = false;

  private animationFrameId: number | null = null;

  constructor() {
    this.images = [
      '_11_background.png',
      '_10_distant_clouds.png',
      '_09_distant_clouds1.png',
      '_08_clouds.png',
      '_07_huge_clouds.png',
      '_06_hill2.png',
      '_05_hill1.png',
      '_04_bushes.png',
      '_03_distant_trees.png',
      '_02_trees.png',
      '_01_ground.png',
    ];
  }
  ngOnInit() {
    this.startAnimationLoop();

    document.querySelector('#bag')?.addEventListener('click', (e) => {
      if (!this.cooldown) {
        this.attempts += 1;
        this.bagLaunched = true;

        this.barPrecision = this.barStrength - 25;

        console.log(this.barPrecision, this.barStrength);

        if (Math.abs(this.barPrecision) <= 10) {
          console.log(this.barPrecision);

          this.lastPunctuation = Math.round(
            5 * (20 - Math.abs(this.barPrecision))
          );
          this.punctuation += this.lastPunctuation;
        } else {
          this.lastPunctuation = 0;
        }

        this.targetPosition.x = 10 * this.barPrecision + this.cartPosition.x;
        this.targetPosition.y = this.cartPosition.y;
      }
    });

    const form = document.querySelector('form');

    document.querySelector('#form-submit')?.addEventListener('click', (e) => {
      const name = document.querySelector('#name').value;

      const data = JSON.parse(localStorage.getItem('users')) ?? [];

      const newData = [...data, { name: name, punctuation: this.punctuation }];

      localStorage.setItem('users', JSON.stringify(newData));

      console.log(newData);
      

      this.users = newData;

      this.attempts = 0;
      this.cooldown = false;
      this.hiddenForm = true;
    });

    this.restoreBagPosition();
  }

  restoreBagPosition() {
    this.bagPosition.x = this.bagRestPosition.x;
    this.bagPosition.y = this.bagRestPosition.y;
    this.barStrength = 0;

    if (this.attempts >= 5) {
      this.cooldown = true;
      this.hiddenForm = false;
    }
  }

  formSubmit(event: SubmitEvent) {
    event.preventDefault();

    console.log('yes');
  }

  startAnimationLoop() {
    const animate = () => {
      // console.log(this.barStrength);

      this.skip = !this.skip;
      if (!this.skip) {
        if (this.bagLaunched && !this.cooldown) {
          this.bagPosition.x +=
            (this.targetPosition.x - this.bagRestPosition.x) /
            this.animationFrames;
          this.bagPosition.y -=
            -(this.targetPosition.y - this.bagRestPosition.y) /
            this.animationFrames;

          if (this.currentFrame <= this.animationFrames / 2) {
            this.bagPosition.y -=
              (this.animationFrames / 2 - this.currentFrame) * 3;
          } else {
            this.bagPosition.y +=
              (this.currentFrame - this.animationFrames / 2) * 3;
          }

          this.currentFrame++;

          if (this.currentFrame >= this.animationFrames) {
            this.bagLaunched = false;
            this.currentFrame = 0;
            this.cooldown = true;

            setTimeout(() => {
              this.cooldown = false;
              this.restoreBagPosition();
            }, 1500);
          }
        } else if (!this.cooldown) {
          if (this.barUp) {
            this.barStrength += Math.random() * 2;

            if (this.barStrength > 50) {
              this.barStrength = 50;
              this.barUp = false;
            }
          } else {
            this.barStrength -= Math.random() * 2;

            if (this.barStrength < 0) {
              this.barStrength = 0;
              this.barUp = true;
            }
          }
        }
      }

      this.animationFrameId = requestAnimationFrame(animate);
    };
    this.animationFrameId = requestAnimationFrame(animate);
  }
}
