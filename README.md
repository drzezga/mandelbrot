# Mandelbrot
App for visualizing the Mandelbrot set and creating videos originally written by Mikołaj Hudko in middle school while learning java. Later fixed up for a university course.

## Prerequisites
 - Java 1.8
 - Maven
 - ffmpeg (best copied into the project root dir)

## Controls when focusing the preview pane
 * `WASD` to move the preview,
 * `R` to zoom in, `F` to zoom out,
 * `Q` to rerender,
 * `X` to increase iteration (accuracy of calculation), `Z` to decrease,
 * `Y` to swap between rendering the Mandelbrot and Julia sets

### `Image` tab
Contains resolution presets and image manipulation options

### `Render settings` tab
Contains settings for:
 * Render engine:
   * Mandelbrot set with float precision
   * Mandelbrot set with double precision
   * Julia set with double precision
   * Color palette display
 * Crosshair display
 * Image supersampling.
 * Coloring algorithm (the `Smooth` option doesn't work well with Julia sets) and color palette

### `Position` tab
Precise values of the parameters used for rendering. All can be set manually

### `Animation` tab
This tab is used for creating animations with ffmpeg. Create camera keyframes on the timeline and edit by right clicking. Move them by dragging and resize the timeline by scrolling. You can set the current position and parameters by clicking `Set` and preview a keyframe by clicking `Go`.
