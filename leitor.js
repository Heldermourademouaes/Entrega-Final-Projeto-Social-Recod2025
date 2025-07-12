class TextReader {
    constructor() {
        this.synth = window.speechSynthesis;
        this.voices = [];
        this.currentUtterance = null;
        this.isReading = false;
        this.currentParagraph = 0;
        this.paragraphs = [];
        this.isNavigating = false; // Flag para controlar navegação
        this.autoAdvance = true; // Flag para controlar avanço automático

        this.init();
    }

    init() {
        this.setupElements();
        this.loadVoices();
        this.getParagraphs();
        this.setupEventListeners();
    }

    setupElements() {
        this.playBtn = document.getElementById('playBtn');
        this.pauseBtn = document.getElementById('pauseBtn');
        this.stopBtn = document.getElementById('stopBtn');
        this.speedSlider = document.getElementById('speedSlider');
        this.speedValue = document.getElementById('speedValue');
        this.voiceSelect = document.getElementById('voiceSelect');
        this.status = document.getElementById('status');
        this.nextBtn = document.getElementById('nextBtn');
        this.prevBtn = document.getElementById('prevBtn');
    }

    loadVoices() {
        const setVoices = () => {
            this.voices = this.synth.getVoices();
            this.populateVoiceSelect();
        };

        setVoices();
        if (this.synth.onvoiceschanged !== undefined) {
            this.synth.onvoiceschanged = setVoices;
        }
    }

    populateVoiceSelect() {
        this.voiceSelect.innerHTML = '';

        const portugueseVoices = this.voices.filter(voice =>
            voice.lang.includes('pt') || voice.lang.includes('br')
        );

        const voicesToUse = portugueseVoices.length ? portugueseVoices : this.voices;

        voicesToUse.forEach((voice, index) => {
            const option = document.createElement('option');
            option.value = this.voices.indexOf(voice);
            option.textContent = `${voice.name} (${voice.lang})`;
            this.voiceSelect.appendChild(option);
        });
    }

    getParagraphs() {
        const contentElements = document.querySelectorAll('h1, h2, h3, p');
        this.paragraphs = Array.from(contentElements).filter(el => el.textContent.trim() !== '');
    }

    refreshContent() {
        this.getParagraphs();
        if (this.isReading) this.stopReading();
    }

    setupEventListeners() {
        this.playBtn?.addEventListener('click', () => this.startReading());
        this.pauseBtn?.addEventListener('click', () => this.pauseReading());
        this.stopBtn?.addEventListener('click', () => this.stopReading());

        this.speedSlider?.addEventListener('input', (e) => {
            this.speedValue.textContent = `${e.target.value}x`;
        });

        this.nextBtn?.addEventListener('click', () => this.nextParagraph());
        this.prevBtn?.addEventListener('click', () => this.previousParagraph());
    }

    startReading() {
        this.getParagraphs();

        if (this.synth.paused) {
            this.synth.resume();
            this.updateStatus('Lendo...');
            return;
        }

        if (this.isReading) return;

        if (!this.paragraphs.length) {
            this.updateStatus('Nenhum conteúdo encontrado para ler');
            return;
        }

        this.isReading = true;
        this.autoAdvance = true;
        this.readCurrentParagraph();
    }

    readCurrentParagraph() {
        // Cancela qualquer síntese em andamento
        this.synth.cancel();

        if (this.currentParagraph >= this.paragraphs.length || this.currentParagraph < 0) {
            this.stopReading();
            return;
        }

        // Remove destaque anterior
        document.querySelectorAll('.reading').forEach(el => el.classList.remove('reading'));

        const paragraph = this.paragraphs[this.currentParagraph];
        paragraph.classList.add('reading');

        const text = paragraph.textContent;
        this.currentUtterance = new SpeechSynthesisUtterance(text);

        const selectedVoiceIndex = this.voiceSelect.value;
        if (selectedVoiceIndex && this.voices[selectedVoiceIndex]) {
            this.currentUtterance.voice = this.voices[selectedVoiceIndex];
        }

        this.currentUtterance.rate = parseFloat(this.speedSlider.value);

        this.currentUtterance.onend = () => {
            paragraph.classList.remove('reading');

            // Só avança automaticamente se ainda estiver lendo E se o avanço automático estiver ativo
            if (this.isReading && this.autoAdvance) {
                this.currentParagraph++;

                if (window.simplifiedReader) {
                    simplifiedReader.currentParagraph = this.currentParagraph;
                    simplifiedReader.highlightCurrentParagraph();
                    simplifiedReader.updateParagraphCounter();
                    simplifiedReader.updateVoteDisplay();
                }

                setTimeout(() => this.readCurrentParagraph(), 500);
            }
        };

        this.currentUtterance.onerror = (event) => {
            console.error('Erro na síntese de voz:', event);
            this.stopReading();
        };

        this.synth.speak(this.currentUtterance);
        this.updateStatus(`Lendo parágrafo ${this.currentParagraph + 1} de ${this.paragraphs.length}`);

        const counter = document.getElementById('paragraphCounter');
        if (counter) {
            counter.textContent = `${this.currentParagraph + 1} / ${this.paragraphs.length}`;
        }
    }

    pauseReading() {
        if (this.synth.speaking) {
            this.synth.pause();
            this.updateStatus('Pausado');
        }
    }

    stopReading() {
        this.synth.cancel();
        this.isReading = false;
        this.autoAdvance = true;
        document.querySelectorAll('.reading').forEach(el => el.classList.remove('reading'));
        this.updateStatus('Pronto para ler');
    }

    nextParagraph() {
        if (this.currentParagraph < this.paragraphs.length - 1) {
            // Desativa o avanço automático temporariamente
            this.autoAdvance = false;
            this.synth.cancel();
            this.currentParagraph++;
            
            // Atualiza integração com simplifiedReader se existir
            if (window.simplifiedReader) {
                simplifiedReader.currentParagraph = this.currentParagraph;
                simplifiedReader.highlightCurrentParagraph();
                simplifiedReader.updateParagraphCounter();
                simplifiedReader.updateVoteDisplay();
            }

            // Atualiza contador visual
            const counter = document.getElementById('paragraphCounter');
            if (counter) {
                counter.textContent = `${this.currentParagraph + 1} / ${this.paragraphs.length}`;
            }

            // Reativa o avanço automático e inicia a leitura
            setTimeout(() => {
                this.autoAdvance = true;
                this.isReading = true;
                this.readCurrentParagraph();
            }, 50);
        }
    }

    previousParagraph() {
        if (this.currentParagraph > 0) {
            // Desativa o avanço automático temporariamente
            this.autoAdvance = false;
            this.synth.cancel();
            this.currentParagraph--;
            
            // Atualiza integração com simplifiedReader se existir
            if (window.simplifiedReader) {
                simplifiedReader.currentParagraph = this.currentParagraph;
                simplifiedReader.highlightCurrentParagraph();
                simplifiedReader.updateParagraphCounter();
                simplifiedReader.updateVoteDisplay();
            }

            // Atualiza contador visual
            const counter = document.getElementById('paragraphCounter');
            if (counter) {
                counter.textContent = `${this.currentParagraph + 1} / ${this.paragraphs.length}`;
            }

            // Reativa o avanço automático e inicia a leitura
            setTimeout(() => {
                this.autoAdvance = true;
                this.isReading = true;
                this.readCurrentParagraph();
            }, 50);
        }
    }

    goToParagraph(index) {
        if (index >= 0 && index < this.paragraphs.length) {
            this.synth.cancel();
            this.currentParagraph = index;
            
            // Atualiza integração com simplifiedReader se existir
            if (window.simplifiedReader) {
                simplifiedReader.currentParagraph = this.currentParagraph;
                simplifiedReader.highlightCurrentParagraph();
                simplifiedReader.updateParagraphCounter();
                simplifiedReader.updateVoteDisplay();
            }

            // Atualiza contador visual
            const counter = document.getElementById('paragraphCounter');
            if (counter) {
                counter.textContent = `${this.currentParagraph + 1} / ${this.paragraphs.length}`;
            }

            if (this.isReading) {
                this.readCurrentParagraph();
            }
        }
    }

    updateStatus(message) {
        this.status.textContent = message;
    }
}

let textReader = null;