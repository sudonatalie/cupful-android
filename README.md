# Cupful

![Cupful header](https://raw.githubusercontent.com/natalieperna/cupful-android/master/images/web_feature_graphic.png)

## Install

[Cupful - Google Play Store](https://play.google.com/store/apps/details?id=com.natalieperna.cupful)

## About

Cupful is an app that allows you to easily convert amounts of ingredients from volume (i.e. cups, ml, tsp, tbsp) to weight (i.e. g, oz, lb), and vice versa, with over 500 different ingredients supported!
It's an essential tool for every baker and chef who values precise measurements in their baking and cooking.

## Screenshots

TODO: Update these.

| Conversion                 | Ingredients                |
|:--------------------------:|:--------------------------:|
| ![Screenshot][screenshot1] | ![Screenshot][screenshot4] |


[screenshot1]: https://github.com/natalieperna/cupful-android/blob/master/images/screenshots/screenshot_1.png
[screenshot4]: https://github.com/natalieperna/cupful-android/blob/master/images/screenshots/screenshot_4.png

## FAQ

### How does Cupful convert measurements from volume to weight, and vice versa?

In order to convert quantities from volume to weight (mass), you need to know the density of the quantity being measured.

```
density = mass ÷ volume
```

Given the value of 2 of those variables, we can calculate the third. Cupful stores the density for many common ingredients, so if you know the weight, it can calculate `volume = mass ÷ density`, and if you know the volume, it can calculate `mass = density x volume`.

These calculations are performed using the [JScience Library](http://jscience.org/).

### Where do Cupful's ingredient densities come from?

The app started with a copy of the public [USDA Food Composition Database](https://ndb.nal.usda.gov/ndb/), which contains the densities of many common foods. Over time, I (the developer of Cupful) have periodically updated these values to match what I see in my own kitchen (which is just a normal kitchen and not a scientific lab). It's anecdotal, but I primarily develop Cupful for my personal use.

### The conversion for "All-Purpose Flour" is wrong!

It seems like every conversion charts say 1 cup of all-purpose flour weighs 120 g, so why does Cupful say it's 140 g?

For context, here are 3 different measurements of 1 cup of all-purpose flour from my kitchen:

| Method                                                                                       | Weight (g) |
|----------------------------------------------------------------------------------------------|------------|
| Scoop from bag directly into cup, shake off excess (wrong)                                   | 151        |
| Pour from bag into cup (wrong)                                                               | 142        |
| Whisk to loosen then lightly spoon into cup, scrape off excess with a knife (correct!)       | 114        |

Cupful originally used the 120 g/cup density, which makes sense if you measure your flour "correctly". Yet, I baked many recipes for several years with this conversion and it frequently disappointed me with too-sticky doughs and too-dense cakes.

I now suspect that approximately 0% of all home bakers actually measure their flour this "correct" way. I also suspect that most conventional recipe writers know this, and write recipes based on the assumption that their readers will measure their flour "incorrectly". As a result, I (personally and anecdotally) find that my baked goods turn out "better" when I convert flour using a density of 140 g/cup, so that's what Cupful does for now.

Exception: Some recipes explicitly note "1 cup of flour, measured correctly\*". In these cases, I do use the 120 g/cup density, which is available in the Cupful app under "Flour, all-purpose, sifted".

This is a surprisingly contentious issue, and your feedback is still welcome, I'm open to modifying this default again in the future.

### The conversion for {some other ingredient} is wrong!

If you suspect a conversion may be incorrect, I would very much appreciate your help correcting it. It may be an ingredient I don't commonly have in my pantry. I don't accept requests to change densities to match *Arbitrary Converter Website* -- the sources are too unreliable.

1. Place a measuring cup on a kitchen scale.
2. Tare/zero the scale.
3. Measure 1 US cup of the ingredient into the measuring cup.
4. Place it back on the scale. Record the weight in grams.
5. Create a [new issue](https://github.com/sudonatalie/cupful-android/issues/new) with your results.

I can't promise that I will change it, or how long the update will take if I do, but I do update ingredients often so your feedback is welcome!

### My favourite ingredient is missing!

If you'd like it included in the Cupful database, refer the previous FAQ and follow the steps to measure its density, and file an issue. I may include it in the next release.

### I have a differeferent questions / request.

Please create a [new issue](https://github.com/sudonatalie/cupful-android/issues/new).
