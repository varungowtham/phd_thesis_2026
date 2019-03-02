settings.outformat = "pdf";
usepackage("mathpazo");
defaultpen(fontsize(11pt));
size(6cm);draw(unitsquare);
label("$A$",(0,0),SW);
label("$B$",(1,0),SE);
label("$C$",(1,1),NE);
label("$D$",(0,1),NW);
label(minipage("\centering This is some text in the same font type as the surrounding text.
",4cm),(1,0.5),E);