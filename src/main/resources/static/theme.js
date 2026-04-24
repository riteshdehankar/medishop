// Apply saved theme on every page load
(function() {
    if (localStorage.getItem('darkMode') === 'true') {
        document.documentElement.setAttribute('data-theme', 'dark');
    }
})();

function applyDarkMode(enabled) {
    document.documentElement.setAttribute('data-theme', enabled ? 'dark' : 'light');
    localStorage.setItem('darkMode', enabled);
}

function applyLanguage(lang) {
    localStorage.setItem('lang', lang);
    const translations = {
        'en': { shop: 'Shop', cart: 'Cart', orders: 'My Orders', logout: 'Logout', welcome: 'Welcome', search: 'Search medicines...', addCart: '+ Add', settings: 'Settings' },
        'hi': { shop: 'दुकान', cart: 'कार्ट', orders: 'मेरे ऑर्डर', logout: 'लॉगआउट', welcome: 'स्वागत', search: 'दवाई खोजें...', addCart: '+ जोड़ें', settings: 'सेटिंग्स' },
        'mr': { shop: 'दुकान', cart: 'कार्ट', orders: 'माझे ऑर्डर', logout: 'लॉगआउट', welcome: 'स्वागत', search: 'औषध शोधा...', addCart: '+ जोडा', settings: 'सेटिंग्ज' },
        'ta': { shop: 'கடை', cart: 'கார்ட்', orders: 'என் ஆர்டர்கள்', logout: 'வெளியேறு', welcome: 'வரவேற்கிறோம்', search: 'மருந்து தேடுங்கள்...', addCart: '+ சேர்', settings: 'அமைப்புகள்' },
        'te': { shop: 'షాప్', cart: 'కార్ట్', orders: 'నా ఆర్డర్లు', logout: 'లాగ్అవుట్', welcome: 'స్వాగతం', search: 'మందులు వెతకండి...', addCart: '+ జోడించు', settings: 'సెట్టింగులు' }
    };
    const t = translations[lang] || translations['en'];
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (t[key]) el.textContent = t[key];
    });
    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const key = el.getAttribute('data-i18n-placeholder');
        if (t[key]) el.placeholder = t[key];
    });
}

// Auto apply language on page load
document.addEventListener('DOMContentLoaded', function() {
    const lang = localStorage.getItem('lang') || 'en';
    applyLanguage(lang);
});