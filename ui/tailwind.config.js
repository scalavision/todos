const defaultTheme = require('tailwindcss/defaultTheme');

function withOpacity(variableName) {
  return ({ opacityValue }) => {
    if (opacityValue !== undefined) {
      return `rgba(var(${variableName}), ${opacityValue})`
    }
    return `rgb(var(${variableName}))`
  }
}

module.exports = {
  content: [
    'index.html'
  ],
  theme: {
    screens: {
      xxxl: '2450px',
      ...defaultTheme.screens,
    },
    extend: {
      animation: {
        tilt: 'tilt 10s infinite linear',
        blob: 'blob 10s infinite linear'
      },
      keyframes: {
        tilt: {
          "0%, 50%, 100%": {
            transform: "rotate(0deg)",
          },
          "25%": {
            transform: "rotate(1deg)"
          },
          "75%": {
            transform: "rotate(-1deg)"
          }
        },
        blob: {
          "0%": {
            transform: "translate(0px, 0px) scale(1)"
          },
          "33%": {
            transform: "translate(30px, -50px) scale(1.15)"
          },
          "66%": {
            transform: "translate(-20px, 20px) scale(0.87)"
          },
          "100%": {
            transform: "translate(0px, 0px) scale(1)"
          }
        }
      },
      colors: {
        ocean: {
          lighter: "#3fbaeb",
          DEFAULT: "#0fa9e6",
          darker: "#0c87b8",
        },
        purpler: {
          white: "#FFFFFF",
          purple: "#3f3cbb",
          midnight: "#131063",
          metal: "#565584",
          'tahiti-blue': '#3ab7bf',
          'cool-white': '#ecebff',
          'bubble-gum': '#ff77e9',
          'copper-rust': '#78dcca'
        }
      },
      boxShadow: {
        sm: '0px 2px 4px 0px rgba(11, 10, 55, 0.15)',
        lg: '0px 8px 20px 0px rgba(18,16,19,0.06)'
      },
      fontFamily: {
        // sans-serif is a fallback
        poppins: "Poppins, sans-serif", // font-headline
        satoshi: "Satoshi, sans-serif",
        inter: "Inter, sans-serif"
      },
      /* color guide:
        https://blog.prototypr.io/basic-ui-color-guide-7612075cc71a
        http://www.instantshift.com/2014/08/26/accent-colors-in-web-design/
        https://uxplanet.org/deciding-color-schemes-for-websites-and-branding-408ae509b23a
      */
      textColor: {
        skin: {
          'text-50': withOpacity('--color-text-50'),
          'text-100': withOpacity('--color-text-100'),

          /* Primary color, the second version if needed */
          'muted-50': withOpacity('--color-muted-50'),
          'muted-100': withOpacity('--color-muted-100'),

        }
      },
      backgroundColor: {
        skin: {

          'bkg-700': withOpacity('--color-bkg-700'),
          'bkg-800': withOpacity('--color-bkg-800'),
          'bkg-900': withOpacity('--color-bkg-900'),

          /* Primary color, the color used mostly, one or two is ideal*/
          'base-700': withOpacity('--color-base-700'),
          'base-800': withOpacity('--color-base-800'),
          'base-900': withOpacity('--color-base-900'),

          /* Primary color, the second version if needed */
          'muted-50': withOpacity('--color-muted-50'),
          'muted-100': withOpacity('--color-muted-100'),

          'fill-inverted': withOpacity('--color-fill-inverted'),

          'button-base': withOpacity('--color-button-base'),
          'button-accent-hover': withOpacity('--color-button-accent-hover'),
          'button-muted': withOpacity('--color-button-muted'),
        }
      },
      borderColor: {
        skin: {
          /* Primary color, the color used mostly, one or two is ideal*/
          'base-700': withOpacity('--color-base-700'),
          'base-800': withOpacity('--color-base-800'),
          'base-900': withOpacity('--color-base-900'),

          /* Primary color, the second version if needed */
          'muted-50': withOpacity('--color-muted-50'),
          'muted-100': withOpacity('--color-muted-100'),
        }
      },

      gradientColorStops: {
        skin: {
          hue: withOpacity('--color-fill')
        }
      }
    },
  },
  plugins: [
    require('@tailwindcss/typography'),
    require('@tailwindcss/forms')
  ],
}
