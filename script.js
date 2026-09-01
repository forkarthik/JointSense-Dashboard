/* ============================================================
   JointSense Dashboard v2 — Interactive Logic
   Chart.js charts, animated counters, sensor waveforms,
   scroll-triggered reveals, navigation, and hybrid form logic
   ============================================================ */

document.addEventListener('DOMContentLoaded', () => {
  // Initialize Lucide icons
  if (window.lucide) lucide.createIcons();

  initNavbar();
  initScrollReveal();
  initCounters();
  initCharts();
  initSensorPanels();
  initHybridForm();
});


/* ============================================================
   NAVBAR
   ============================================================ */
function initNavbar() {
  const navbar = document.getElementById('navbar');
  const navLinks = document.querySelectorAll('.navbar-links a');
  const sections = document.querySelectorAll('section[id]');
  const navToggle = document.getElementById('navToggle');
  const navMenu = document.getElementById('navLinks');

  // Scroll effect
  window.addEventListener('scroll', () => {
    navbar.classList.toggle('scrolled', window.scrollY > 50);

    // Active section tracking
    let current = '';
    sections.forEach(section => {
      const top = section.offsetTop - 120;
      if (window.scrollY >= top) current = section.id;
    });

    navLinks.forEach(link => {
      link.classList.remove('active');
      if (link.getAttribute('href') === '#' + current) {
        link.classList.add('active');
      }
    });
  });

  // Mobile toggle
  if (navToggle) {
    navToggle.addEventListener('click', () => {
      navMenu.style.display = navMenu.style.display === 'flex' ? 'none' : 'flex';
    });
  }
}


/* ============================================================
   SCROLL REVEAL (IntersectionObserver)
   ============================================================ */
function initScrollReveal() {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, { threshold: 0.1, rootMargin: '0px 0px -40px 0px' });

  document.querySelectorAll('.reveal').forEach(el => observer.observe(el));
}


/* ============================================================
   ANIMATED COUNTERS
   ============================================================ */
function initCounters() {
  const counters = document.querySelectorAll('.counter-value');

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting && !entry.target.dataset.animated) {
        entry.target.dataset.animated = 'true';
        animateCounter(entry.target);
      }
    });
  }, { threshold: 0.5 });

  counters.forEach(c => observer.observe(c));
}

function animateCounter(el) {
  const target = parseFloat(el.dataset.target);
  const decimals = parseInt(el.dataset.decimals || '0');
  const duration = 2000;
  const start = performance.now();

  function update(now) {
    const elapsed = now - start;
    const progress = Math.min(elapsed / duration, 1);
    // Ease out cubic
    const eased = 1 - Math.pow(1 - progress, 3);
    const current = eased * target;

    el.textContent = decimals > 0 ? current.toFixed(decimals) : Math.round(current).toLocaleString();

    if (progress < 1) requestAnimationFrame(update);
  }

  requestAnimationFrame(update);
}


/* ============================================================
   HYBRID FORM LOGIC (Interactive Elements)
   ============================================================ */
function initHybridForm() {
  const checkboxes = document.querySelectorAll('.checkbox-pill input[type="checkbox"]');
  const scales = document.querySelectorAll('.severity-scale');
  const inputs = document.querySelectorAll('.form-section input[type="number"], .form-section select');

  function calculateHybridScore() {
    let score = 68; // Base sensor score
    
    // Add points based on severity scales
    scales.forEach(scale => {
      const activeBtn = scale.querySelector('.severity-btn[class*="active-"]');
      if (activeBtn) {
        if (activeBtn.classList.contains('active-1')) score += 3;
        if (activeBtn.classList.contains('active-2')) score += 6;
        if (activeBtn.classList.contains('active-3')) score += 10;
      }
    });

    // Add points for checkboxes
    checkboxes.forEach(cb => {
      if (cb.checked) score += 2;
    });

    // Add points for demographics, prior injuries, and labs
    inputs.forEach(input => {
      const formGroup = input.closest('.form-group');
      if (!formGroup) return;
      const label = formGroup.querySelector('label');
      if (!label) return;
      
      const labelText = label.textContent.toLowerCase();

      if (input.tagName === 'SELECT') {
        if (input.value.includes('tear') || input.value.includes('injury')) score += 5;
        if (labelText.includes('gender') && input.value === 'Female') score += 2;
        if (labelText.includes('occupation') && (input.value.includes('Farming') || input.value.includes('Manual'))) score += 3;
        if (labelText.includes('kl grade') && input.value.includes('KL') && !input.value.includes('0')) {
          score += parseInt(input.value.replace('KL ', '')) * 4;
        }
      }

      if (input.tagName === 'INPUT' && input.type === 'number' && input.value) {
        if (labelText.includes('age')) {
          const age = parseInt(input.value);
          if (age > 60) score += 4;
          else if (age > 45) score += 2;
        }
        if (labelText.includes('bmi')) {
          const bmi = parseFloat(input.value);
          if (bmi >= 30) score += 4;
          else if (bmi >= 25) score += 2;
        }
        if (labelText.includes('stiffness')) {
          const mins = parseInt(input.value);
          if (mins > 30) score += 3;
        }
        if (labelText.includes('esr')) {
          const esr = parseFloat(input.value);
          if (esr > 20) score += 3;
        }
        if (labelText.includes('crp')) {
          const crp = parseFloat(input.value);
          if (crp > 10) score += 5;
          else if (crp > 3) score += 2;
        }
      }
    });

    score = Math.min(100, Math.round(score));
    
    const scoreEl = document.getElementById('hybridScore');
    if (scoreEl) {
      scoreEl.textContent = score;
      
      const statusText = scoreEl.parentElement.nextElementSibling;
      if (score >= 85) {
        scoreEl.parentElement.style.color = 'var(--accent-rose)';
        statusText.style.color = 'var(--accent-rose)';
        statusText.textContent = 'Severe Risk · Immediate specialist referral';
      } else if (score >= 70) {
        scoreEl.parentElement.style.color = 'var(--accent-amber)';
        statusText.style.color = 'var(--accent-amber)';
        statusText.textContent = 'Moderate Risk · Refer for imaging';
      } else {
        scoreEl.parentElement.style.color = 'var(--accent-teal)';
        statusText.style.color = 'var(--accent-teal)';
        statusText.textContent = 'Low Risk · Routine monitoring';
      }
    }
  }

  // Checkbox Pills
  checkboxes.forEach(cb => {
    cb.addEventListener('change', (e) => {
      if (e.target.checked) {
        e.target.parentElement.classList.add('checked');
      } else {
        e.target.parentElement.classList.remove('checked');
      }
      calculateHybridScore();
    });
  });

  // Severity Scales
  scales.forEach(scale => {
    const buttons = scale.querySelectorAll('.severity-btn');
    buttons.forEach((btn, index) => {
      btn.addEventListener('click', (e) => {
        e.preventDefault(); 
        buttons.forEach(b => {
          b.className = 'severity-btn'; 
        });
        btn.classList.add(`active-${index}`);
        calculateHybridScore();
      });
    });
  });

  // Other Inputs
  inputs.forEach(input => {
    input.addEventListener('change', calculateHybridScore);
  });
}


/* ============================================================
   CHART.JS — Global Defaults
   ============================================================ */
function getChartDefaults() {
  return {
    color: '#94a3b8',
    borderColor: 'rgba(255,255,255,0.06)',
    font: { family: "'Inter', sans-serif" },
  };
}

Chart.defaults.color = '#94a3b8';
Chart.defaults.borderColor = 'rgba(255,255,255,0.06)';
Chart.defaults.font.family = "'Inter', sans-serif";
Chart.defaults.plugins.legend.labels.usePointStyle = true;
Chart.defaults.plugins.legend.labels.padding = 20;
Chart.defaults.plugins.tooltip.backgroundColor = 'rgba(6,9,15,0.9)';
Chart.defaults.plugins.tooltip.borderColor = 'rgba(0,212,170,0.2)';
Chart.defaults.plugins.tooltip.borderWidth = 1;
Chart.defaults.plugins.tooltip.cornerRadius = 8;
Chart.defaults.plugins.tooltip.padding = 12;
Chart.defaults.plugins.tooltip.titleFont = { weight: '600' };
Chart.defaults.animation.duration = 1500;
Chart.defaults.animation.easing = 'easeOutQuart';


/* ============================================================
   CHARTS INITIALIZATION
   ============================================================ */
function initCharts() {
  createCostChart();
  createSensitivityChart();
  createRadarChart();
  createKLChart();
  createProjectionChart();
  createTimeChart();
  createScaleChart();
}


// ---- 1. Cost Comparison Bar Chart ----
function createCostChart() {
  const ctx = document.getElementById('costChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['X-Ray', 'MRI', 'Ultrasound', 'Clinical Exam', 'Wearable', 'JointSense'],
      datasets: [{
        data: [1500, 8000, 3000, 500, 800, 50],
        backgroundColor: [
          'rgba(148, 163, 184, 0.3)',
          'rgba(148, 163, 184, 0.25)',
          'rgba(148, 163, 184, 0.2)',
          'rgba(148, 163, 184, 0.25)',
          'rgba(148, 163, 184, 0.2)',
          'rgba(0, 212, 170, 0.6)',
        ],
        borderColor: [
          'rgba(148,163,184,0.4)',
          'rgba(148,163,184,0.4)',
          'rgba(148,163,184,0.4)',
          'rgba(148,163,184,0.4)',
          'rgba(148,163,184,0.4)',
          'rgba(0,212,170,1)',
        ],
        borderWidth: [1, 1, 1, 1, 1, 2],
        borderRadius: 6,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => `₹${ctx.parsed.y.toLocaleString()}`
          }
        }
      },
      scales: {
        y: {
          type: 'logarithmic',
          min: 30,
          max: 12000,
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => '₹' + v.toLocaleString(),
            font: { size: 11 }
          }
        },
        x: {
          grid: { display: false },
          ticks: { font: { size: 11 } }
        }
      }
    }
  });
}


// ---- 2. Sensitivity Bar Chart ----
function createSensitivityChart() {
  const ctx = document.getElementById('sensitivityChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['X-Ray', 'MRI', 'Ultrasound', 'Clinical Exam', 'JointSense (Sensor)', 'JointSense (Hybrid)'],
      datasets: [{
        data: [35, 85, 65, 40, 89, 96],
        backgroundColor: [
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(0,180,216,0.4)',
          'rgba(0,212,170,0.6)',
        ],
        borderColor: [
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(0,180,216,0.8)',
          'rgba(0,212,170,1)',
        ],
        borderWidth: [1, 1, 1, 1, 2, 2],
        borderRadius: 6,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => `${ctx.parsed.y}% sensitivity`
          }
        }
      },
      scales: {
        y: {
          min: 0,
          max: 100,
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => v + '%',
            font: { size: 11 }
          }
        },
        x: {
          grid: { display: false },
          ticks: { font: { size: 11 } }
        }
      }
    },
    plugins: [{
      id: 'thresholdLine',
      afterDraw(chart) {
        const { ctx, scales: { y } } = chart;
        const yPos = y.getPixelForValue(80);
        ctx.save();
        ctx.setLineDash([6, 4]);
        ctx.strokeStyle = 'rgba(245,158,11,0.5)';
        ctx.lineWidth = 1.5;
        ctx.beginPath();
        ctx.moveTo(chart.chartArea.left, yPos);
        ctx.lineTo(chart.chartArea.right, yPos);
        ctx.stroke();
        ctx.fillStyle = 'rgba(245,158,11,0.7)';
        ctx.font = '11px Inter';
        ctx.fillText('Clinical threshold (80%)', chart.chartArea.left + 8, yPos - 8);
        ctx.restore();
      }
    }]
  });
}


// ---- 3. Radar / Spider Chart ----
function createRadarChart() {
  const ctx = document.getElementById('radarChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'radar',
    data: {
      labels: ['Early Sensitivity', 'Cost Efficiency', 'Portability', 'Speed', 'Clinical Context', 'Training Ease'],
      datasets: [
        {
          label: 'JointSense (Hybrid)',
          data: [5, 5, 5, 4, 4.5, 5],
          backgroundColor: 'rgba(0,212,170,0.12)',
          borderColor: 'rgba(0,212,170,0.9)',
          borderWidth: 2.5,
          pointBackgroundColor: 'rgba(0,212,170,1)',
          pointBorderColor: '#06090f',
          pointBorderWidth: 2,
          pointRadius: 5,
        },
        {
          label: 'X-Ray',
          data: [1.5, 2, 0.5, 4, 1, 1.5],
          backgroundColor: 'rgba(148,163,184,0.05)',
          borderColor: 'rgba(148,163,184,0.4)',
          borderWidth: 1.5,
          pointBackgroundColor: 'rgba(148,163,184,0.6)',
          pointRadius: 3,
          borderDash: [4, 4],
        },
        {
          label: 'MRI',
          data: [4.5, 0.5, 0.2, 1, 2, 0.5],
          backgroundColor: 'rgba(139,92,246,0.05)',
          borderColor: 'rgba(139,92,246,0.4)',
          borderWidth: 1.5,
          pointBackgroundColor: 'rgba(139,92,246,0.6)',
          pointRadius: 3,
          borderDash: [4, 4],
        },
        {
          label: 'Clinical Exam',
          data: [2, 4, 4.5, 3, 5, 3],
          backgroundColor: 'rgba(245,158,11,0.05)',
          borderColor: 'rgba(245,158,11,0.4)',
          borderWidth: 1.5,
          pointBackgroundColor: 'rgba(245,158,11,0.6)',
          pointRadius: 3,
          borderDash: [4, 4],
        },
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: { padding: 16, font: { size: 11 } }
        }
      },
      scales: {
        r: {
          min: 0,
          max: 5.5,
          ticks: {
            stepSize: 1,
            display: false,
          },
          grid: { color: 'rgba(255,255,255,0.06)' },
          angleLines: { color: 'rgba(255,255,255,0.06)' },
          pointLabels: {
            font: { size: 11, weight: '500' },
            color: '#94a3b8',
          }
        }
      }
    }
  });
}


// ---- 4. KL Grade Grouped Bar Chart ----
function createKLChart() {
  const ctx = document.getElementById('klChart');
  if (!ctx) return;

  const stages = ['Pre-clinical (KL 0)', 'Early (KL 1)', 'Mild (KL 2)', 'Moderate (KL 3)', 'Severe (KL 4)'];

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: stages,
      datasets: [
        {
          label: 'X-Ray',
          data: [5, 15, 60, 90, 98],
          backgroundColor: 'rgba(148,163,184,0.2)',
          borderColor: 'rgba(148,163,184,0.4)',
          borderWidth: 1,
          borderRadius: 4,
        },
        {
          label: 'MRI',
          data: [40, 70, 92, 98, 99],
          backgroundColor: 'rgba(139,92,246,0.2)',
          borderColor: 'rgba(139,92,246,0.4)',
          borderWidth: 1,
          borderRadius: 4,
        },
        {
          label: 'JointSense (Sensor)',
          data: [45, 78, 91, 96, 99],
          backgroundColor: 'rgba(0,180,216,0.3)',
          borderColor: 'rgba(0,180,216,0.8)',
          borderWidth: 2,
          borderRadius: 4,
        },
        {
          label: 'JointSense (Hybrid)',
          data: [52, 85, 96, 98, 99],
          backgroundColor: 'rgba(0,212,170,0.5)',
          borderColor: 'rgba(0,212,170,1)',
          borderWidth: 2,
          borderRadius: 4,
        },
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: { padding: 16, font: { size: 10 } }
        },
        tooltip: {
          callbacks: {
            label: (ctx) => `${ctx.dataset.label}: ${ctx.parsed.y}%`
          }
        }
      },
      scales: {
        y: {
          min: 0,
          max: 110,
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => v + '%',
            font: { size: 11 }
          }
        },
        x: {
          grid: { display: false },
          ticks: { font: { size: 10 } }
        }
      }
    },
    plugins: [{
      id: 'earlyZone',
      beforeDraw(chart) {
        const { ctx, chartArea, scales: { x } } = chart;
        ctx.save();
        const x0 = chartArea.left;
        const x1 = x.getPixelForValue(1) + (x.getPixelForValue(1) - x.getPixelForValue(0)) / 2;
        ctx.fillStyle = 'rgba(0,212,170,0.03)';
        ctx.fillRect(x0, chartArea.top, x1 - x0, chartArea.bottom - chartArea.top);
        ctx.fillStyle = 'rgba(0,212,170,0.3)';
        ctx.font = 'bold 11px Inter';
        ctx.fillText('TARGET: Early Detection', x0 + 12, chartArea.top + 18);
        ctx.restore();
      }
    }]
  });
}


// ---- 5. 5-Year Cost Projection Line Chart ----
function createProjectionChart() {
  const ctx = document.getElementById('projectionChart');
  if (!ctx) return;

  const years = ['Year 1', 'Year 2', 'Year 3', 'Year 4', 'Year 5'];
  const costXR = [0.9, 1.8, 2.7, 3.6, 4.5];
  const costJS = [0.03, 0.06, 0.09, 0.12, 0.15];
  const savings = costXR.map((v, i) => +(v - costJS[i]).toFixed(2));

  new Chart(ctx, {
    type: 'line',
    data: {
      labels: years,
      datasets: [
        {
          label: 'X-Ray baseline',
          data: costXR,
          borderColor: 'rgba(244,63,94,0.7)',
          backgroundColor: 'rgba(244,63,94,0.05)',
          borderWidth: 2,
          fill: false,
          pointBackgroundColor: 'rgba(244,63,94,1)',
          pointRadius: 5,
          pointBorderColor: '#06090f',
          pointBorderWidth: 2,
          tension: 0.3,
        },
        {
          label: 'JointSense',
          data: costJS,
          borderColor: 'rgba(0,212,170,0.9)',
          backgroundColor: 'rgba(0,212,170,0.05)',
          borderWidth: 2.5,
          fill: false,
          pointBackgroundColor: 'rgba(0,212,170,1)',
          pointRadius: 5,
          pointBorderColor: '#06090f',
          pointBorderWidth: 2,
          tension: 0.3,
        },
        {
          label: 'Cumulative Savings',
          data: savings,
          borderColor: 'rgba(245,158,11,0.7)',
          borderWidth: 1.5,
          borderDash: [6, 4],
          fill: false,
          pointBackgroundColor: 'rgba(245,158,11,0.8)',
          pointRadius: 4,
          tension: 0.3,
        },
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: { padding: 16, font: { size: 11 } }
        },
        tooltip: {
          callbacks: {
            label: (ctx) => `${ctx.dataset.label}: ₹${ctx.parsed.y} Cr`
          }
        }
      },
      scales: {
        y: {
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => '₹' + v + ' Cr',
            font: { size: 11 }
          }
        },
        x: {
          grid: { display: false },
          ticks: { font: { size: 11 } }
        }
      }
    }
  });
}


// ---- 6. Screening Time Bar Chart ----
function createTimeChart() {
  const ctx = document.getElementById('timeChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['X-Ray*', 'MRI', 'Ultrasound', 'Clinical Exam', 'JointSense (Sensor)', 'JointSense (Hybrid)'],
      datasets: [{
        data: [5, 45, 20, 15, 8, 12],
        backgroundColor: [
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(148,163,184,0.25)',
          'rgba(139,92,246,0.3)',
          'rgba(139,92,246,0.6)',
        ],
        borderColor: [
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(148,163,184,0.3)',
          'rgba(139,92,246,0.6)',
          'rgba(139,92,246,1)',
        ],
        borderWidth: [1, 1, 1, 1, 2, 2],
        borderRadius: 6,
      }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => `${ctx.parsed.x} minutes`
          }
        }
      },
      scales: {
        x: {
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => v + ' min',
            font: { size: 11 }
          }
        },
        y: {
          grid: { display: false },
          ticks: { font: { size: 11 } }
        }
      }
    }
  });
}


// ---- 7. Scale / Volume Cost Chart ----
function createScaleChart() {
  const ctx = document.getElementById('scaleChart');
  if (!ctx) return;

  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['10 units', '100 units', '1,000 units', '10,000 units'],
      datasets: [
        {
          label: 'Unit Cost',
          data: [8600, 6000, 3500, 1350],
          backgroundColor: [
            'rgba(148,163,184,0.25)',
            'rgba(0,180,216,0.3)',
            'rgba(0,212,170,0.4)',
            'rgba(0,212,170,0.6)',
          ],
          borderColor: [
            'rgba(148,163,184,0.4)',
            'rgba(0,180,216,0.6)',
            'rgba(0,212,170,0.8)',
            'rgba(0,212,170,1)',
          ],
          borderWidth: [1, 1, 1, 2],
          borderRadius: 6,
          yAxisID: 'y',
        },
        {
          label: 'Cost Reduction',
          data: [0, 30, 59, 84],
          type: 'line',
          borderColor: 'rgba(245,158,11,0.8)',
          backgroundColor: 'rgba(245,158,11,0.05)',
          borderWidth: 2,
          pointBackgroundColor: 'rgba(245,158,11,1)',
          pointRadius: 5,
          pointBorderColor: '#06090f',
          pointBorderWidth: 2,
          tension: 0.4,
          fill: false,
          yAxisID: 'y1',
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: {
          position: 'bottom',
          labels: { padding: 16, font: { size: 11 } }
        },
        tooltip: {
          callbacks: {
            label: (ctx) => {
              if (ctx.dataset.yAxisID === 'y1') return `${ctx.parsed.y}% reduction`;
              return `₹${ctx.parsed.y.toLocaleString()} per unit`;
            }
          }
        }
      },
      scales: {
        y: {
          position: 'left',
          grid: { color: 'rgba(255,255,255,0.04)' },
          ticks: {
            callback: (v) => '₹' + v.toLocaleString(),
            font: { size: 11 }
          }
        },
        y1: {
          position: 'right',
          min: 0,
          max: 100,
          grid: { display: false },
          ticks: {
            callback: (v) => v + '%',
            font: { size: 11 }
          }
        },
        x: {
          grid: { display: false },
          ticks: { font: { size: 11 } }
        }
      }
    }
  });
}


/* ============================================================
   ANIMATED SENSOR WAVEFORM PANELS
   ============================================================ */
function initSensorPanels() {
  const sensors = [
    { id: 'sensorGyro', valueId: 'gyroValue', type: 'gyro' },
    { id: 'sensorTemp', valueId: 'tempValue', type: 'temp' },
    { id: 'sensorPiezo', valueId: 'piezoValue', type: 'piezo' },
    { id: 'sensorFlex', valueId: 'flexValue', type: 'flex' },
    { id: 'sensorEMG', valueId: 'emgValue', type: 'emg' },
  ];

  sensors.forEach(s => {
    const canvas = document.getElementById(s.id);
    if (!canvas) return;
    const valueEl = document.getElementById(s.valueId);
    startSensorAnimation(canvas, valueEl, s.type);
  });
}

function startSensorAnimation(canvas, valueEl, type) {
  const ctx = canvas.getContext('2d');
  const bufferSize = 300;
  let data = new Float32Array(bufferSize).fill(0);
  let frame = 0;

  function resize() {
    const rect = canvas.parentElement.getBoundingClientRect();
    canvas.width = rect.width * (window.devicePixelRatio || 1);
    canvas.height = rect.height * (window.devicePixelRatio || 1);
    ctx.scale(window.devicePixelRatio || 1, window.devicePixelRatio || 1);
  }
  resize();
  window.addEventListener('resize', resize);

  const colors = {
    gyro: { line: '#00d4aa', fill: 'rgba(0,212,170,0.08)', glow: 'rgba(0,212,170,0.3)' },
    temp: { line: '#f59e0b', fill: 'rgba(245,158,11,0.08)', glow: 'rgba(245,158,11,0.3)' },
    piezo: { line: '#00b4d8', fill: 'rgba(0,180,216,0.08)', glow: 'rgba(0,180,216,0.3)' },
    flex: { line: '#8b5cf6', fill: 'rgba(139,92,246,0.08)', glow: 'rgba(139,92,246,0.3)' },
    emg: { line: '#f43f5e', fill: 'rgba(244,63,94,0.08)', glow: 'rgba(244,63,94,0.3)' },
  };

  function generateValue(t) {
    switch (type) {
      case 'gyro':
        return Math.sin(t * 0.05) * 80 * Math.sin(t * 0.008) + (Math.random() - 0.5) * 15;
      case 'temp': {
        const base = 32.5 + Math.sin(t * 0.01) * 1.5;
        return base + (Math.random() - 0.5) * 0.08;
      }
      case 'piezo': {
        const burst = Math.sin(t * 0.03) > 0.6 ? Math.random() * 0.8 : 0;
        return burst + Math.random() * 0.05;
      }
      case 'flex':
        return 45 + 40 * Math.sin(t * 0.02) + 15 * Math.sin(t * 0.007) + (Math.random() - 0.5) * 2;
      case 'emg': {
        const env = Math.max(0, Math.sin(t * 0.025)) * 0.8;
        return env * (Math.random() - 0.5) * 2 + (Math.random() - 0.5) * 0.08;
      }
      default:
        return 0;
    }
  }

  function getDisplayValue(v) {
    switch (type) {
      case 'gyro': return `${v.toFixed(1)} °/s`;
      case 'temp': return `${v.toFixed(1)} °C`;
      case 'piezo': return `${v.toFixed(2)} V`;
      case 'flex': return `${v.toFixed(1)}°`;
      case 'emg': return `${(Math.abs(v) * 100).toFixed(0)} µV`;
      default: return v.toFixed(2);
    }
  }

  function draw() {
    frame++;
    const newVal = generateValue(frame);

    for (let i = 0; i < bufferSize - 1; i++) data[i] = data[i + 1];
    data[bufferSize - 1] = newVal;

    if (valueEl && frame % 3 === 0) {
      valueEl.textContent = getDisplayValue(newVal);
    }

    const w = canvas.parentElement.clientWidth;
    const h = canvas.parentElement.clientHeight;

    ctx.clearRect(0, 0, w, h);

    let min = Infinity, max = -Infinity;
    for (let i = 0; i < bufferSize; i++) {
      if (data[i] < min) min = data[i];
      if (data[i] > max) max = data[i];
    }
    const range = max - min || 1;
    const padding = 8;

    const col = colors[type];

    ctx.beginPath();
    ctx.moveTo(0, h);
    for (let i = 0; i < bufferSize; i++) {
      const x = (i / (bufferSize - 1)) * w;
      const y = padding + ((max - data[i]) / range) * (h - padding * 2);
      ctx.lineTo(x, y);
    }
    ctx.lineTo(w, h);
    ctx.closePath();
    ctx.fillStyle = col.fill;
    ctx.fill();

    ctx.shadowColor = col.glow;
    ctx.shadowBlur = 6;
    ctx.beginPath();
    for (let i = 0; i < bufferSize; i++) {
      const x = (i / (bufferSize - 1)) * w;
      const y = padding + ((max - data[i]) / range) * (h - padding * 2);
      if (i === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    }
    ctx.strokeStyle = col.line;
    ctx.lineWidth = 1.5;
    ctx.stroke();
    ctx.shadowBlur = 0;

    requestAnimationFrame(draw);
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        draw();
        observer.disconnect();
      }
    });
  }, { threshold: 0.2 });

  observer.observe(canvas);
}
