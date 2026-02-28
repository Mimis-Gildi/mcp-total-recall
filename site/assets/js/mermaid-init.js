document.addEventListener("DOMContentLoaded", function () {
  if (typeof mermaid === "undefined") return;

  mermaid.initialize({ startOnLoad: false, theme: "dark" });

  mermaid.run().then(function () {
    if (typeof svgPanZoom === "undefined") return;
    document.querySelectorAll(".mermaid svg").forEach(function (svg) {
      svg.style.height = "70vh";
      svgPanZoom(svg, {
        zoomEnabled: false,
        controlIconsEnabled: true,
        zoomScaleSensitivity: 0.5,
        dblClickZoomEnabled: true,
        fit: true,
        center: true
      });
    });
  });
});
